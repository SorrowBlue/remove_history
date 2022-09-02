package com.sorrowblue.comicviewer.domain.usecase.interactor

import androidx.paging.PagingData
import com.sorrowblue.comicviewer.domain.model.Response
import com.sorrowblue.comicviewer.domain.model.file.File
import com.sorrowblue.comicviewer.domain.model.file.LoadFileRequest
import com.sorrowblue.comicviewer.domain.repository.FileRepository
import com.sorrowblue.comicviewer.domain.usecase.LoadFileUseCase
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

internal class LoadFileInteractor @Inject constructor(
    private val repository: FileRepository,
) : LoadFileUseCase() {

    override fun run(request: LoadFileRequest): Response<Flow<PagingData<File>>> {
        return repository.pagingDataFlow(request.pagingConfig, request.library, request.bookshelf)
    }
}
