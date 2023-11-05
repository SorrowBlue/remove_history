package com.sorrowblue.comicviewer.folder

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.sorrowblue.comicviewer.domain.model.bookshelf.BookshelfId
import com.sorrowblue.comicviewer.domain.model.file.File
import com.sorrowblue.comicviewer.file.FileInfoBottomSheet
import com.sorrowblue.comicviewer.file.FileInfoSheet
import com.sorrowblue.comicviewer.file.component.FileContent
import com.sorrowblue.comicviewer.file.component.FileContentType
import com.sorrowblue.comicviewer.file.rememberSideSheetFileState
import com.sorrowblue.comicviewer.folder.section.FolderAppBar
import com.sorrowblue.comicviewer.folder.section.FolderAppBarUiState
import com.sorrowblue.comicviewer.folder.section.Sort
import com.sorrowblue.comicviewer.folder.section.SortSheet
import com.sorrowblue.comicviewer.framework.designsystem.icon.ComicIcons
import com.sorrowblue.comicviewer.framework.designsystem.icon.undraw.UndrawResumeFolder
import com.sorrowblue.comicviewer.framework.designsystem.theme.ComicTheme
import com.sorrowblue.comicviewer.framework.ui.EmptyContent
import com.sorrowblue.comicviewer.framework.ui.add
import com.sorrowblue.comicviewer.framework.ui.asWindowInsets
import com.sorrowblue.comicviewer.framework.ui.material3.TopAppBarDefaults
import com.sorrowblue.comicviewer.framework.ui.material3.pinnedScrollBehavior
import com.sorrowblue.comicviewer.framework.ui.paging.isEmptyData
import com.sorrowblue.comicviewer.framework.ui.paging.isLoadedData
import com.sorrowblue.comicviewer.framework.ui.pullrefresh.PullRefreshIndicator
import com.sorrowblue.comicviewer.framework.ui.pullrefresh.PullRefreshState
import com.sorrowblue.comicviewer.framework.ui.pullrefresh.pullRefresh
import com.sorrowblue.comicviewer.framework.ui.pullrefresh.rememberPullRefreshState
import com.sorrowblue.comicviewer.framework.ui.responsive.ResponsiveScaffold
import com.sorrowblue.comicviewer.framework.ui.responsive.rememberResponsiveScaffoldState

internal data class FolderScreenUiState(
    val folderAppBarUiState: FolderAppBarUiState = FolderAppBarUiState(),
    val openSortSheet: Boolean = false,
    val currentSort: Sort = Sort.NAME_ASC,
    val fileContentType: FileContentType = FileContentType.Grid(),
)

@Composable
internal fun FolderRoute(
    contentPadding: PaddingValues,
    onSearchClick: (BookshelfId, String) -> Unit,
    onSettingsClick: () -> Unit,
    onBackClick: () -> Unit,
    onRestoreComplete: () -> Unit,
    onClickFile: (File, Int) -> Unit,
    viewModel: FolderViewModel = hiltViewModel(),
) {
    val lazyPagingItems = viewModel.pagingDataFlow.collectAsLazyPagingItems()
    val uiState by viewModel.uiState.collectAsState()
    val lazyGridState = rememberLazyGridState()
    val isRefreshing =
        remember(lazyPagingItems.loadState.refresh) { lazyPagingItems.loadState.refresh is LoadState.Loading }
    val pullRefreshState = rememberPullRefreshState(isRefreshing, lazyPagingItems::refresh)
    FolderScreen(
        contentPadding = contentPadding,
        uiState = uiState,
        lazyPagingItems = lazyPagingItems,
        onSearchClick = { onSearchClick(viewModel.bookshelfId, viewModel.path) },
        onSortSheetDismissRequest = viewModel::onSortSheetDismissRequest,
        onSortChange = viewModel::onSortChange,
        onClickFile = {
            onClickFile.invoke(it, lazyGridState.firstVisibleItemIndex)
        },
        lazyGridState = lazyGridState,
        isRefreshing = isRefreshing,
        pullRefreshState = pullRefreshState,
        onFileListChange = viewModel::toggleFileListType,
        onSettingsClick = onSettingsClick,
        onGridSizeChange = viewModel::onGridSizeChange,
        onBackClick = onBackClick,
        onSortClick = viewModel::openSort,
    )

    if (uiState.openSortSheet) {
        SortSheet(
            currentSort = uiState.currentSort,
            onDismissRequest = viewModel::onSortSheetDismissRequest,
            onClick = viewModel::onSortChange
        )
    }
    LaunchedEffect(lazyPagingItems.loadState) {
        if (0 <= viewModel.position && lazyPagingItems.loadState.refresh is LoadState.NotLoading && lazyPagingItems.itemCount > 0) {
            val position = viewModel.position
            viewModel.position = -1
            onRestoreComplete()
            lazyGridState.scrollToItem(position)
        } else if (lazyPagingItems.isLoadedData && viewModel.isScrollableTop) {
            viewModel.isScrollableTop = false
            lazyGridState.scrollToItem(0)
        }
    }

    val sort by viewModel.sort.collectAsState()
    LaunchedEffect(sort) {
        if (!viewModel.isSkipFirstRefresh) {
            lazyPagingItems.refresh()
        }
    }
}

@Composable
internal fun FolderScreen(
    contentPadding: PaddingValues,
    uiState: FolderScreenUiState,
    lazyPagingItems: LazyPagingItems<File>,
    onSearchClick: () -> Unit,
    onSortSheetDismissRequest: () -> Unit,
    onSortChange: (Sort) -> Unit,
    onClickFile: (File) -> Unit,
    lazyGridState: LazyGridState,
    isRefreshing: Boolean,
    pullRefreshState: PullRefreshState = rememberPullRefreshState(isRefreshing, { }),
    onFileListChange: () -> Unit,
    onSettingsClick: () -> Unit,
    onGridSizeChange: () -> Unit,
    onBackClick: () -> Unit,
    onSortClick: () -> Unit,
    onReadLaterClick: (File) -> Unit = {},
    onFavoriteClick: (File) -> Unit = {},
    onOpenFolderClick: (File) -> Unit = {},
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val state = rememberResponsiveScaffoldState(sideSheetState = rememberSideSheetFileState())
    ResponsiveScaffold(
        state = state,
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            FolderAppBar(
                uiState = uiState.folderAppBarUiState,
                onFileListChange = onFileListChange,
                onBackClick = onBackClick,
                onSearchClick = onSearchClick,
                onGridSizeChange = onGridSizeChange,
                onSortClick = onSortClick,
                onSettingsClick = onSettingsClick,
                windowInsets = contentPadding.asWindowInsets(),
                scrollBehavior = scrollBehavior,
            )
        },
        sideSheet = { file, innerPadding ->
            FileInfoSheet(
                file = file,
                contentPadding = innerPadding.add(paddingValues = PaddingValues(top = ComicTheme.dimension.margin)),
                onCloseClick = { state.sheetState.hide() },
                onReadLaterClick = { onReadLaterClick(file) },
                onFavoriteClick = { onFavoriteClick(file) },
                onOpenFolderClick = { onOpenFolderClick(file) }
            )
        },
        bottomSheet = { file ->
            FileInfoBottomSheet(
                file = file,
                onReadLaterClick = { onReadLaterClick(file) },
                onFavoriteClick = { onFavoriteClick(file) },
                onOpenFolderClick = { onOpenFolderClick(file) },
                onDismissRequest = { state.sheetState.hide() },
            )
        },
        contentWindowInsets = contentPadding.asWindowInsets(),
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
        ) {
            if (lazyPagingItems.isEmptyData) {
                EmptyContent(
                    imageVector = ComicIcons.UndrawResumeFolder,
                    text = "「○○○○○」には何もありません。",
                    contentPadding = it
                )
            } else {
                FileContent(
                    type = uiState.fileContentType,
                    lazyPagingItems = lazyPagingItems,
                    contentPadding = it,
                    onClickItem = onClickFile,
                    onLongClickItem = { state.sheetState.show(it) },
                    state = lazyGridState
                )
            }
            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
                scale = true,
                modifier = Modifier
                    .padding(it)
                    .align(Alignment.TopCenter)
            )
        }
    }
}
