package com.sorrowblue.comicviewer.domain.interactor

import com.sorrowblue.comicviewer.domain.repository.FileRepository
import com.sorrowblue.comicviewer.domain.usecase.AddReadLaterUseCase
import com.sorrowblue.comicviewer.framework.Resource
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class AddReadLaterInteractor @Inject constructor(
    private val repository: FileRepository
) : AddReadLaterUseCase() {

    override fun run(request: Request): Flow<Resource<Unit, Error>> {
        return repository.addReadLater(request.bookshelfId, request.path).mapFold({
            Resource.Success(Unit)
        }, {
            Resource.Error(Error.System)
        })
    }
}

fun <D, E : Resource.AppError, R> Flow<Resource<D, E>>.mapFold(
    onSuccess: (D) -> R,
    onError: (E) -> R
): Flow<R> {
    return map {
        when (it) {
            is Resource.Error -> onError(it.error)
            is Resource.Success -> onSuccess(it.data)
        }
    }
}
