package com.sorrowblue.comicviewer.bookshelf

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.SupportingPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberSupportingPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import com.sorrowblue.comicviewer.bookshelf.section.BookshelfInfoSheet
import com.sorrowblue.comicviewer.bookshelf.section.BookshelfMainSheet
import com.sorrowblue.comicviewer.bookshelf.section.BookshelfRemoveDialog
import com.sorrowblue.comicviewer.domain.model.BookshelfFolder
import com.sorrowblue.comicviewer.domain.model.Scan
import com.sorrowblue.comicviewer.domain.model.bookshelf.BookshelfId
import com.sorrowblue.comicviewer.domain.model.bookshelf.InternalStorage
import com.sorrowblue.comicviewer.domain.model.file.fakeFolder
import com.sorrowblue.comicviewer.domain.usecase.ScanBookshelfUseCase
import com.sorrowblue.comicviewer.domain.usecase.bookshelf.GetBookshelfInfoUseCase
import com.sorrowblue.comicviewer.domain.usecase.bookshelf.RemoveBookshelfUseCase
import com.sorrowblue.comicviewer.domain.usecase.paging.PagingBookshelfFolderUseCase
import com.sorrowblue.comicviewer.feature.bookshelf.R
import com.sorrowblue.comicviewer.framework.designsystem.icon.ComicIcons
import com.sorrowblue.comicviewer.framework.designsystem.theme.LocalDimension
import com.sorrowblue.comicviewer.framework.ui.CanonicalScaffold
import com.sorrowblue.comicviewer.framework.ui.DialogController
import com.sorrowblue.comicviewer.framework.ui.NavTabHandler
import com.sorrowblue.comicviewer.framework.ui.add
import com.sorrowblue.comicviewer.framework.ui.material3.PreviewTheme
import com.sorrowblue.comicviewer.framework.ui.material3.SettingsButton
import com.sorrowblue.comicviewer.framework.ui.material3.adaptive.navigation.BackHandlerForNavigator
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

interface BookshelfScreenNavigator {
    fun onSettingsClick()
    fun onFabClick()
    fun onBookshelfClick(bookshelfId: BookshelfId, path: String)
    fun onEditClick(bookshelfId: BookshelfId)
}


@Parcelize
data object BookshelfScreen : Screen {

    data class State @OptIn(ExperimentalMaterial3AdaptiveApi::class) constructor(
        val pagingDataFlow: Flow<PagingData<BookshelfFolder>>,
        val navigator: ThreePaneScaffoldNavigator<BookshelfFolder>,
        val snackbarHostState: SnackbarHostState,
        val scope: CoroutineScope,
        val removeDialogController: DialogController<BookshelfFolder?>,
        val lazyGridState: LazyGridState,
        val eventSink: (Event) -> Unit,
    ) : CircuitUiState

    sealed class Event : CircuitUiEvent {
        data object FabClicked : Event()
        data object SettingsClicked : Event()
        data class BookshelfClicked(val bookshelfId: BookshelfId, val path: String) : Event()
        data class InfoClicked(val bookshelfFolder: BookshelfFolder) : Event()
        data object RemoveClicked : Event()
        data object EditClicked : Event()
        data object CloseClicked : Event()
        data object ScanClicked : Event()
        data object Dismiss : Event()
        data object ConfirmClicked : Event()
        data object NavClicked : Event()
    }
}

@CircuitInject(BookshelfScreen::class, SingletonComponent::class)
class BookshelfPresenter @AssistedInject constructor(
    private val pagingBookshelfFolderUseCase: PagingBookshelfFolderUseCase,
    private val removeBookshelfUseCase: RemoveBookshelfUseCase,
    private val getBookshelfInfoUseCase: GetBookshelfInfoUseCase,
    private val scanBookshelfUseCase: ScanBookshelfUseCase,
    @Assisted private val navigator: Navigator,
) : Presenter<BookshelfScreen.State> {

    @CircuitInject(BookshelfScreen::class, SingletonComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): BookshelfPresenter
    }

    @OptIn(ExperimentalMaterial3AdaptiveApi::class)
    @Composable
    override fun present(): BookshelfScreen.State {
        val scope = rememberCoroutineScope()
        val pagingDataFlow = pagingBookshelfFolderUseCase
            .execute(PagingBookshelfFolderUseCase.Request(PagingConfig(20)))
            .cachedIn(scope)

        val navigator = rememberSupportingPaneScaffoldNavigator<BookshelfFolder>()
        val snackbarHostState = remember { SnackbarHostState() }

        val bookshelfIdLiveData =
            MutableStateFlow(navigator.currentDestination?.content?.bookshelf?.id)
        val context = LocalContext.current
        val dialogController = remember { DialogController<BookshelfFolder?>(null) }
        val lazyGridState = rememberLazyGridState()
        return BookshelfScreen.State(
            pagingDataFlow = pagingDataFlow,
            navigator = navigator,
            scope = scope,
            snackbarHostState = snackbarHostState,
            removeDialogController = remember { DialogController(null) },
            lazyGridState = lazyGridState
        ) { event ->
            when (event) {
                is BookshelfScreen.Event.BookshelfClicked -> TODO()
                BookshelfScreen.Event.CloseClicked -> {
                    navigator.navigateBack()
                }

                BookshelfScreen.Event.ConfirmClicked -> {
                    val bookshelf = navigator.currentDestination?.content!!.bookshelf
                    scope.launch {
                        removeBookshelfUseCase.execute(RemoveBookshelfUseCase.Request(bookshelf))
                        dialogController.dismiss()
                        navigator.navigateBack()
                        snackbarHostState.showSnackbar(
                            context.getString(R.string.bookshelf_msg_delete, bookshelf.displayName)
                        )
                    }
                }

                BookshelfScreen.Event.Dismiss -> {
                    dialogController.dismiss()
                }

                BookshelfScreen.Event.EditClicked -> TODO()
                BookshelfScreen.Event.FabClicked -> TODO()
                is BookshelfScreen.Event.InfoClicked -> {
                    bookshelfIdLiveData.value = event.bookshelfFolder.bookshelf.id
                    navigator.navigateTo(SupportingPaneScaffoldRole.Extra, event.bookshelfFolder)
                }

                BookshelfScreen.Event.NavClicked -> {
                    if (lazyGridState.canScrollBackward) {
                        scope.launch {
                            lazyGridState.scrollToItem(0)
                        }
                    }
                }

                BookshelfScreen.Event.RemoveClicked -> {
                    navigator.currentDestination?.content?.let { dialogController.show(it) }
                }

                BookshelfScreen.Event.ScanClicked -> {
                    navigator.currentDestination?.content?.let {
                        scope.launch {
                            scanBookshelfUseCase.execute(
                                ScanBookshelfUseCase.Request(
                                    it.folder,
                                    Scan.ALL
                                )
                            )
                        }
                    }
                }

                BookshelfScreen.Event.SettingsClicked -> TODO()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@CircuitInject(BookshelfScreen::class, SingletonComponent::class)
@Composable
internal fun BookshelfScreen(state: BookshelfScreen.State, modifier: Modifier) {
    BookshelfScreen(
        navigator = state.navigator,
        lazyPagingItems = state.pagingDataFlow.collectAsLazyPagingItems(),
        lazyGridState = state.lazyGridState,
        snackbarHostState = state.snackbarHostState,
        onFabClick = { state.eventSink(BookshelfScreen.Event.FabClicked) },
        onSettingsClick = { state.eventSink(BookshelfScreen.Event.SettingsClicked) },
        onBookshelfClick = { id, path ->
            state.eventSink(BookshelfScreen.Event.BookshelfClicked(id, path))
        },
        onBookshelfInfoClick = { state.eventSink(BookshelfScreen.Event.InfoClicked(it)) },
        onInfoSheetRemoveClick = { state.eventSink(BookshelfScreen.Event.RemoveClicked) },
        onInfoSheetEditClick = { state.eventSink(BookshelfScreen.Event.EditClicked) },
        onInfoSheetCloseClick = { state.eventSink(BookshelfScreen.Event.CloseClicked) },
        onInfoSheetScanClick = { state.eventSink(BookshelfScreen.Event.ScanClicked) },
    )
    val removeDialogController = state.removeDialogController
    if (removeDialogController.isShow) {
        BookshelfRemoveDialog(
            title = removeDialogController.value!!.bookshelf.displayName,
            onDismissRequest = { state.eventSink(BookshelfScreen.Event.Dismiss) },
            onConfirmClick = { state.eventSink(BookshelfScreen.Event.ConfirmClicked) },
        )
    }

    BackHandlerForNavigator(navigator = state.navigator)

    NavTabHandler { state.eventSink(BookshelfScreen.Event.NavClicked) }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun BookshelfScreen(
    navigator: ThreePaneScaffoldNavigator<BookshelfFolder>,
    lazyPagingItems: LazyPagingItems<BookshelfFolder>,
    snackbarHostState: SnackbarHostState,
    onFabClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onBookshelfClick: (BookshelfId, String) -> Unit,
    onBookshelfInfoClick: (BookshelfFolder) -> Unit,
    onInfoSheetRemoveClick: () -> Unit,
    onInfoSheetEditClick: () -> Unit,
    onInfoSheetScanClick: () -> Unit,
    onInfoSheetCloseClick: () -> Unit,
    lazyGridState: LazyGridState = rememberLazyGridState(),
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    CanonicalScaffold(
        navigator = navigator,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.bookshelf_list_title))
                },
                actions = {
                    SettingsButton(onClick = onSettingsClick)
                },
                windowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top),
                scrollBehavior = scrollBehavior,
            )
        },
        floatingActionButton = {
            val expanded by remember(lazyGridState) {
                derivedStateOf { !lazyGridState.canScrollForward || !lazyGridState.canScrollBackward }
            }
            ExtendedFloatingActionButton(
                expanded = expanded,
                onClick = onFabClick,
                text = { Text(text = stringResource(R.string.bookshelf_btn_add)) },
                icon = { Icon(imageVector = ComicIcons.Add, contentDescription = null) },
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.End))
            )
        },
        extraPane = { innerPadding ->
            var bookshelfFolder by rememberSaveable { mutableStateOf<BookshelfFolder?>(null) }
            LaunchedEffect(key1 = navigator.currentDestination) {
                navigator.currentDestination?.content?.let { bookshelfFolder = it }
            }
            bookshelfFolder?.let {
                BookshelfInfoSheet(
                    contentPadding = innerPadding,
                    scaffoldDirective = navigator.scaffoldDirective,
                    bookshelfFolder = it,
                    onRemoveClick = onInfoSheetRemoveClick,
                    onEditClick = onInfoSheetEditClick,
                    onScanClick = onInfoSheetScanClick,
                    onCloseClick = onInfoSheetCloseClick
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { contentPadding ->
        val dimension = LocalDimension.current
        val innerPadding = contentPadding.add(
            PaddingValues(
                start = dimension.margin,
                top = dimension.margin,
                end = dimension.margin,
                bottom = dimension.margin + FabSpace
            )
        )
        BookshelfMainSheet(
            lazyPagingItems = lazyPagingItems,
            lazyGridState = lazyGridState,
            onBookshelfClick = onBookshelfClick,
            onBookshelfInfoClick = onBookshelfInfoClick,
            innerPadding = innerPadding
        )
    }
}

private val FabSpace get() = 72.dp

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@PreviewScreenSizes
@Composable
private fun PreviewBookshelfScreen() {
    PreviewTheme {
        val pagingDataFlow = remember {
            List(15) {
                BookshelfFolder(
                    InternalStorage(BookshelfId(it), "name"),
                    fakeFolder()
                )
            }.toPersistentList().let {
                MutableStateFlow(PagingData.from(it))
            }
        }
        val lazyPagingItems = pagingDataFlow.collectAsLazyPagingItems()
        BookshelfScreen(
            snackbarHostState = remember { SnackbarHostState() },
            navigator = rememberSupportingPaneScaffoldNavigator<BookshelfFolder>(),
            lazyPagingItems = lazyPagingItems,
            onFabClick = {},
            onSettingsClick = {},
            onBookshelfClick = { _, _ -> },
            onBookshelfInfoClick = {},
            onInfoSheetRemoveClick = {},
            onInfoSheetEditClick = {},
            onInfoSheetScanClick = {},
            onInfoSheetCloseClick = {},
        )
    }
}
