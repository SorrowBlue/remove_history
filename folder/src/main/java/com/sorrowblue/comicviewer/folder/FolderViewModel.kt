package com.sorrowblue.comicviewer.folder

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.sorrowblue.comicviewer.domain.Base64.decodeFromBase64
import com.sorrowblue.comicviewer.domain.entity.bookshelf.BookshelfId
import com.sorrowblue.comicviewer.domain.model.ScanType
import com.sorrowblue.comicviewer.domain.usecase.ScanBookshelfUseCase
import com.sorrowblue.comicviewer.domain.usecase.bookshelf.GetBookshelfFolderUseCase
import com.sorrowblue.comicviewer.domain.usecase.paging.PagingFileUseCase
import com.sorrowblue.comicviewer.domain.usecase.paging.PagingQueryFileUseCase
import com.sorrowblue.comicviewer.domain.usecase.settings.ManageFolderDisplaySettingsUseCase
import com.sorrowblue.comicviewer.file.list.FileListViewModel
import com.sorrowblue.comicviewer.framework.ui.navigation.SupportSafeArgs
import com.sorrowblue.comicviewer.framework.ui.navigation.navArgs
import com.sorrowblue.comicviewer.framework.ui.navigation.stateIn
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
internal class FolderViewModel @Inject constructor(
    pagingFileUseCase: PagingFileUseCase,
    pagingQueryFileUseCase: PagingQueryFileUseCase,
    manageFolderDisplaySettingsUseCase: ManageFolderDisplaySettingsUseCase,
    getBookshelfFolderUseCase: GetBookshelfFolderUseCase,
    private val scanBookshelfUseCase: ScanBookshelfUseCase,
    override val savedStateHandle: SavedStateHandle,
) : FileListViewModel(manageFolderDisplaySettingsUseCase), SupportSafeArgs {

    private val args: FolderFragmentArgs by navArgs()
    var position = args.position
    override val transitionName = args.transitionName

    private val serverFolderFlow = getBookshelfFolderUseCase.execute(
        GetBookshelfFolderUseCase.Request(BookshelfId(args.serverId), args.path.decodeFromBase64())
    ).mapNotNull { it.dataOrNull }

    private val serverFlow = serverFolderFlow.map { it.bookshelf }.stateIn { null }

    val folderFlow = serverFolderFlow.map { it.folder }.stateIn { null }

    val titleFlow = serverFlow.mapNotNull { it?.displayName }.stateIn { "" }

    val subTitleFlow = folderFlow.mapNotNull { it?.name }.stateIn { "" }

    override val pagingDataFlow = serverFolderFlow.flatMapLatest {
        pagingFileUseCase.execute(
            PagingFileUseCase.Request(PagingConfig(100), it.bookshelf, it.folder)
        )
    }.cachedIn(viewModelScope)
    var query = ""
    var parent: String? = null

    val pagingQueryDataFlow = serverFolderFlow.flatMapLatest {
        pagingQueryFileUseCase.execute(
            PagingQueryFileUseCase.Request(
                PagingConfig(100),
                it.bookshelf,
                { parent },
                { query }
            )
        )
    }.cachedIn(viewModelScope)

    fun fullScan(scanType: ScanType, done: (String) -> Unit) {
        val folder = folderFlow.value ?: return
        viewModelScope.launch {
            scanBookshelfUseCase.execute(ScanBookshelfUseCase.Request(folder, scanType))
                .first().dataOrNull?.let { done.invoke(it) }
        }
    }
}
