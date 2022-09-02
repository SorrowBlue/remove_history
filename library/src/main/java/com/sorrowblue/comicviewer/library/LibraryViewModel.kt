package com.sorrowblue.comicviewer.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sorrowblue.comicviewer.domain.model.library.Library
import com.sorrowblue.comicviewer.domain.model.library.LoadLibraryRequest
import com.sorrowblue.comicviewer.domain.usecase.LoadLibraryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@HiltViewModel
internal class LibraryViewModel @Inject constructor(
    loadLibraryUseCase: LoadLibraryUseCase
) : ViewModel() {

    val libraryInfo: Flow<PagingData<Library>> =
        loadLibraryUseCase.execute(LoadLibraryRequest(PagingConfig(10))).fold({
            it.cachedIn(viewModelScope)
        }, {
            emptyFlow()
        })

    fun bookshelfToLibraryItem(library: Library): LibraryItem {
        return LibraryItem(
            library,
            library.name.ifEmpty { "${library.host}/${library.path}" },
            library.preview
        )
    }
}
