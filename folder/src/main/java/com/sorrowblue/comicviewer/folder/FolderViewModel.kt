package com.sorrowblue.comicviewer.folder

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sorrowblue.comicviewer.domain.entity.Scan
import com.sorrowblue.comicviewer.domain.entity.file.File
import com.sorrowblue.comicviewer.domain.entity.file.IFolder
import com.sorrowblue.comicviewer.domain.entity.settings.FolderDisplaySettings
import com.sorrowblue.comicviewer.domain.entity.settings.SortType
import com.sorrowblue.comicviewer.domain.usecase.AddReadLaterUseCase
import com.sorrowblue.comicviewer.domain.usecase.ScanBookshelfUseCase
import com.sorrowblue.comicviewer.domain.usecase.file.GetFileUseCase
import com.sorrowblue.comicviewer.domain.usecase.paging.PagingFileUseCase
import com.sorrowblue.comicviewer.domain.usecase.settings.ManageFolderDisplaySettingsUseCase
import com.sorrowblue.comicviewer.file.FileListType
import com.sorrowblue.comicviewer.folder.navigation.FolderArgs
import com.sorrowblue.comicviewer.folder.section.FileInfoSheetUiState
import com.sorrowblue.comicviewer.folder.section.FolderAppBarUiState
import com.sorrowblue.comicviewer.folder.section.Sort
import com.sorrowblue.comicviewer.folder.section.SortSheetUiState
import com.sorrowblue.comicviewer.framework.Resource
import com.sorrowblue.comicviewer.framework.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flattenConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import logcat.logcat

@HiltViewModel
class FolderViewModel @Inject constructor(
    getFileUseCase: GetFileUseCase,
    pagingFileUseCase: PagingFileUseCase,
    private val scanBookshelfUseCase: ScanBookshelfUseCase,
    private val displaySettingsUseCase: ManageFolderDisplaySettingsUseCase,
    private val addReadLaterUseCase: AddReadLaterUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val args = FolderArgs(savedStateHandle)
    val bookshelfId = args.bookshelfId
    val path = args.path

    @OptIn(ExperimentalCoroutinesApi::class)
    val pagingDataFlow: Flow<PagingData<File>> = pagingFileUseCase.execute(
        PagingFileUseCase.Request(PagingConfig(30), bookshelfId, path)
    ).filterSuccess().flattenConcat().cachedIn(viewModelScope)


    private val _uiState = MutableStateFlow(
        FolderScreenUiState(
            folderAppBarUiState = FolderAppBarUiState(
                "",
                runBlocking { displaySettingsUseCase.settings.first().toFileListType() }),
            sortSheetUiState = SortSheetUiState.Hide,
            fileInfoSheetUiState = FileInfoSheetUiState.Hide,
            fileListType = runBlocking { displaySettingsUseCase.settings.first().toFileListType() }
        )
    )
    init {
        viewModelScope.launch {
            displaySettingsUseCase.settings.map(FolderDisplaySettings::toFileListType)
                .distinctUntilChanged().collectLatest {
                    _uiState.value = _uiState.value.copy(
                        folderAppBarUiState = _uiState.value.folderAppBarUiState.copy(fileListType = runBlocking {
                            displaySettingsUseCase.settings.first().toFileListType()
                        }),
                        fileListType = it
                    )
                }
        }
        viewModelScope.launch {
            getFileUseCase.execute(GetFileUseCase.Request(bookshelfId, path)).first().onSuccess {
                _uiState.value = _uiState.value.copy(
                    folderAppBarUiState = _uiState.value.folderAppBarUiState.copy(title = it.name)
                )
            }
        }
    }

    val uiState = _uiState.asStateFlow()

    var isSkipFirstRefresh = true
    var isScrollableTop = false

    val displaySettings = displaySettingsUseCase.settings.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        runBlocking { displaySettingsUseCase.settings.first() }
    )


    fun openSort() {
        val uiState = _uiState.value
        _uiState.value = uiState.copy(
            sortSheetUiState = SortSheetUiState.Show(
                displaySettings.value.sortType.toSort()
            )
        )
    }

    val sort =
        displaySettingsUseCase.settings.distinctUntilChangedBy(FolderDisplaySettings::sortType)
            .map { it.sortType.toSort() }
            .stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                runBlocking { displaySettingsUseCase.settings.first().sortType.toSort() }
            )

    fun onSortChange(sort: Sort) {
        isScrollableTop = true
        isSkipFirstRefresh = false
        onSortSheetDismissRequest()
        viewModelScope.launch {
            val sortType = when (sort) {
                Sort.NAME_ASC -> SortType.NAME(true)
                Sort.NAME_DESC -> SortType.NAME(false)
                Sort.SIZE_DESC -> SortType.SIZE(true)
                Sort.SIZE_ASC -> SortType.SIZE(false)
                Sort.DATE_ASC -> SortType.DATE(true)
                Sort.DATE_DESC -> SortType.DATE(false)
            }
            displaySettingsUseCase.edit { it.copy(sortType = sortType) }
        }
    }

    fun onSortSheetDismissRequest() {
        val uiState = _uiState.value
        _uiState.value = uiState.copy(sortSheetUiState = SortSheetUiState.Hide)
    }

    fun onFileInfoSheetDismissRequest() {
        val uiState = _uiState.value
        _uiState.value = uiState.copy(fileInfoSheetUiState = FileInfoSheetUiState.Hide)
    }

    fun onClickLongFile(file: File) {
        val uiState = _uiState.value
        _uiState.value = uiState.copy(fileInfoSheetUiState = FileInfoSheetUiState.Show(file))
    }

    fun onAddReadLaterClick(file: File) {
        onFileInfoSheetDismissRequest()
        viewModelScope.launch {
            addReadLaterUseCase.execute(AddReadLaterUseCase.Request(file.bookshelfId, file.path))
                .first()
        }
    }

    fun toggleFileListType() {
        viewModelScope.launch {
            displaySettingsUseCase.edit {
                it.copy(
                    display = when (it.display) {
                        FolderDisplaySettings.Display.GRID -> FolderDisplaySettings.Display.LIST
                        FolderDisplaySettings.Display.LIST -> FolderDisplaySettings.Display.GRID
                    }
                )
            }
        }
    }

    fun scan() {
        viewModelScope.launch {
//            scanBookshelfUseCase.execute(
//                ScanBookshelfUseCase.Request(
//                    file.first() as IFolder,
//                    Scan.ALL
//                )
//            ).collect()
        }
    }

    fun onGridSizeChange() {
        viewModelScope.launch {
            displaySettingsUseCase.edit {
                it.copy(
                    spanCount = when (it.spanCount) {
                        2 -> 3
                        3 -> 4
                        else -> 2
                    }
                )
            }
        }
    }
}

private fun SortType.toSort(): Sort {
    return when (this) {
        is SortType.DATE -> if (isAsc) Sort.DATE_ASC else Sort.DATE_DESC
        is SortType.NAME -> if (isAsc) Sort.NAME_ASC else Sort.NAME_DESC
        is SortType.SIZE -> if (isAsc) Sort.SIZE_ASC else Sort.SIZE_DESC
    }
}

private fun <T, E : Resource.AppError> Flow<Resource<T, E>>.filterSuccess(): Flow<T> {
    return filter {
        it is Resource.Success<T>
    }.map {
        (it as Resource.Success<T>).data
    }
}

private fun FolderDisplaySettings.toFileListType(): FileListType {
    return when (display) {
        FolderDisplaySettings.Display.GRID -> FileListType.Grid(spanCount)
        FolderDisplaySettings.Display.LIST -> FileListType.List
    }
}
