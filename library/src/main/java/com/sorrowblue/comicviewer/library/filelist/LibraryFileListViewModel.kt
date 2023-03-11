package com.sorrowblue.comicviewer.library.filelist

import com.sorrowblue.comicviewer.domain.entity.file.File
import com.sorrowblue.comicviewer.framework.ui.fragment.PagingViewModel
import kotlinx.coroutines.flow.Flow

abstract class LibraryFileListViewModel : PagingViewModel<File>() {

    abstract val isAuthenticated: Flow<Boolean>
}
