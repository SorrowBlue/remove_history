package com.sorrowblue.comicviewer.feature.bookshelf.edit

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.sorrowblue.comicviewer.feature.bookshelf.edit.section.AuthMethod
import com.sorrowblue.comicviewer.feature.bookshelf.edit.section.BookshelfEditorUiState
import com.sorrowblue.comicviewer.feature.bookshelf.edit.section.DeviceStorageInfoEditor
import com.sorrowblue.comicviewer.feature.bookshelf.edit.section.SmbServerInfoEditor
import com.sorrowblue.comicviewer.framework.designsystem.icon.ComicIcons
import com.sorrowblue.comicviewer.framework.designsystem.theme.ComicTheme
import com.sorrowblue.comicviewer.framework.ui.flow.CollectAsEffect

sealed interface BookshelfEditScreenUiState {

    data object Loading : BookshelfEditScreenUiState

    data object Complete : BookshelfEditScreenUiState

    data class Editing(
        val editorUiState: BookshelfEditorUiState = BookshelfEditorUiState.SmbServer(),
    ) : BookshelfEditScreenUiState
}

sealed interface UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent
}

@Composable
internal fun BookshelfEditRoute(
    viewModel: BookshelfEditViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onComplete: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()
    val activityResultLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            it.data?.data?.let { uri ->
                viewModel.updateUri(uri)
            } ?: run {
                TODO()
            }
        }
    val openDocumentTreeIntent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
        flags =
            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
    }
    val snackbarHostState = remember { SnackbarHostState() }
    viewModel.uiEvents.CollectAsEffect {
        when (it) {
            is UiEvent.ShowSnackbar -> snackbarHostState.showSnackbar(it.message)
        }
    }
    BookshelfEditScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onBackClick = onBackClick,
        onDisplayNameChange = viewModel::onDisplayNameChanged,
        onSelectFolderClick = { activityResultLauncher.launch(openDocumentTreeIntent) },
        onHostChange = viewModel::onHostChanged,
        onPortChange = viewModel::onPortChanged,
        onPathChange = viewModel::onPathChange,
        onAuthMethodChange = viewModel::onAuthMethodChange,
        onDomainChange = viewModel::onDomainChange,
        onUsernameChange = viewModel::onUsernameChange,
        onPasswordChange = viewModel::onPasswordChange,
        onSaveClick = viewModel::save
    )
    if (uiState is BookshelfEditScreenUiState.Complete) {
        onComplete()
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun BookshelfEditScreen(
    uiState: BookshelfEditScreenUiState = BookshelfEditScreenUiState.Editing(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onBackClick: () -> Unit = {},
    onDisplayNameChange: (String) -> Unit = {},
    onSelectFolderClick: () -> Unit = {},
    onHostChange: (String) -> Unit = {},
    onPortChange: (String) -> Unit = {},
    onPathChange: (String) -> Unit = {},
    onAuthMethodChange: (AuthMethod) -> Unit = {},
    onDomainChange: (String) -> Unit = {},
    onUsernameChange: (String) -> Unit = {},
    onPasswordChange: (String) -> Unit = {},
    onSaveClick: () -> Unit = {},
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text(text = "本棚の編集") },
                navigationIcon = {
                    IconButton(onBackClick) {
                        Icon(imageVector = ComicIcons.ArrowBack, contentDescription = "Back")
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { contentPaddings ->
        when (uiState) {
            is BookshelfEditScreenUiState.Editing ->
                when (uiState.editorUiState) {
                    is BookshelfEditorUiState.DeviceStorage ->
                        DeviceStorageInfoEditor(
                            modifier = Modifier
                                .padding(contentPaddings)
                                .padding(horizontal = ComicTheme.dimension.margin),
                            uiState = uiState.editorUiState,
                            onDisplayNameChange = onDisplayNameChange,
                            onSelectFolderClick = onSelectFolderClick,
                            onSaveClick = onSaveClick
                        )

                    is BookshelfEditorUiState.SmbServer ->
                        SmbServerInfoEditor(
                            modifier = Modifier.padding(contentPaddings),
                            uiState = uiState.editorUiState,
                            onDisplayNameChange = onDisplayNameChange,
                            onHostChange = onHostChange,
                            onPortChange = onPortChange,
                            onPathChange = onPathChange,
                            onAuthMethodChange = onAuthMethodChange,
                            onDomainChange = onDomainChange,
                            onUsernameChange = onUsernameChange,
                            onPasswordChange = onPasswordChange,
                            onSaveClick = onSaveClick
                        )
                }

            is BookshelfEditScreenUiState.Complete -> {}

            BookshelfEditScreenUiState.Loading -> {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(contentPaddings)
                ) {
                    CircularProgressIndicator(
                        Modifier
                            .align(Alignment.TopCenter)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewBookshelfEditScreen() {
    ComicTheme {
        Surface {
            BookshelfEditScreen()
        }
    }
}

@Preview
@Composable
fun PreviewBookshelfEditScreenLoading() {
    ComicTheme {
        Surface {
            BookshelfEditScreen(BookshelfEditScreenUiState.Loading)
        }
    }
}
