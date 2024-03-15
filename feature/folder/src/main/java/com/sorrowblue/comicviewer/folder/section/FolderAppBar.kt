package com.sorrowblue.comicviewer.folder.section

import android.os.Parcelable
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.sorrowblue.comicviewer.feature.folder.R
import com.sorrowblue.comicviewer.file.component.FileContentType
import com.sorrowblue.comicviewer.framework.designsystem.icon.ComicIcons
import com.sorrowblue.comicviewer.framework.ui.material3.BackButton
import com.sorrowblue.comicviewer.framework.ui.material3.OverflowMenu
import com.sorrowblue.comicviewer.framework.ui.material3.OverflowMenuItem
import com.sorrowblue.comicviewer.framework.ui.material3.PlainTooltipBox
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class FolderAppBarUiState(
    val title: String = "",
    val display: FileContentType = FileContentType.Grid(FileContentType.GridSize.Medium),
    val fileContentType: FileContentType = FileContentType.Grid(FileContentType.GridSize.Medium),
    val showHiddenFile: Boolean = false,
) : Parcelable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FolderAppBar(
    uiState: FolderAppBarUiState,
    onBackClick: () -> Unit,
    onSearchClick: () -> Unit,
    onSortClick: () -> Unit,
    onFileListChange: () -> Unit,
    onGridSizeChange: () -> Unit,
    onHideFileClick: () -> Unit,
    onSettingsClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
) {
    TopAppBar(
        title = { Text(text = uiState.title) },
        navigationIcon = { BackButton(onClick = onBackClick) },
        actions = {
            PlainTooltipBox(tooltipContent = { Text(stringResource(R.string.folder_action_search)) }) {
                IconButton(onClick = onSearchClick) {
                    Icon(ComicIcons.Search, stringResource(R.string.folder_action_search))
                }
            }
            IconButton(onClick = onSortClick) {
                Icon(ComicIcons.SortByAlpha, "sort")
            }
            OverflowMenu {
                if (uiState.fileContentType is FileContentType.Grid) {
                    OverflowMenuItem(
                        text = stringResource(
                            id = com.sorrowblue.comicviewer.feature.file.R.string.file_list_label_switch_list_view
                        ),
                        icon = ComicIcons.ViewList,
                        onClick = onFileListChange
                    )
                } else {
                    OverflowMenuItem(
                        text = stringResource(
                            id = com.sorrowblue.comicviewer.feature.file.R.string.file_list_label_switch_grid_view
                        ),
                        icon = ComicIcons.GridView,
                        onClick = onFileListChange
                    )
                }
                if (uiState.fileContentType is FileContentType.Grid) {
                    OverflowMenuItem(
                        text = stringResource(R.string.folder_action_change_grid_size),
                        icon = ComicIcons.Grid4x4,
                        onClick = onGridSizeChange
                    )
                }
                DropdownMenuItem(
                    text = { Text(text = "隠しファイルを表示") },
                    leadingIcon = {
                        Icon(imageVector = ComicIcons.FolderOff, contentDescription = null)
                    },
                    trailingIcon = {
                        Checkbox(
                            checked = uiState.showHiddenFile,
                            onCheckedChange = {
                                onHideFileClick()
                                state.collapse()
                            }
                        )
                    },
                    onClick = {
                        onHideFileClick()
                        state.collapse()
                    }
                )
                OverflowMenuItem(
                    text = stringResource(R.string.folder_action_settings),
                    icon = ComicIcons.Settings,
                    onClick = onSettingsClick
                )
            }
        },
        windowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top),
        scrollBehavior = scrollBehavior,
    )
}
