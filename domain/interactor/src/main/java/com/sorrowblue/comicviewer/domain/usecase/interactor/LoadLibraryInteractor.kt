package com.sorrowblue.comicviewer.domain.usecase.interactor

import androidx.paging.PagingData
import com.sorrowblue.comicviewer.domain.model.Response
import com.sorrowblue.comicviewer.domain.model.library.Library
import com.sorrowblue.comicviewer.domain.model.library.LoadLibraryRequest
import com.sorrowblue.comicviewer.domain.repository.LibraryRepository
import com.sorrowblue.comicviewer.domain.usecase.LoadLibraryUseCase
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

internal class LoadLibraryInteractor @Inject constructor(
    private val repository: LibraryRepository,
) : LoadLibraryUseCase() {

    override fun run(request: LoadLibraryRequest): Response<Flow<PagingData<Library>>> {
        return repository.pagingDataFlow(request.pagingConfig)
    }
}
