package com.sorrowblue.comicviewer.domain.interactor.bookshelf

import com.sorrowblue.comicviewer.domain.entity.bookshelf.Bookshelf
import com.sorrowblue.comicviewer.domain.entity.file.Folder
import com.sorrowblue.comicviewer.domain.repository.BookshelfRepository
import com.sorrowblue.comicviewer.domain.usecase.bookshelf.RegisterBookshelfUseCase
import com.sorrowblue.comicviewer.framework.Resource
import com.sorrowblue.comicviewer.framework.fold
import com.sorrowblue.comicviewer.framework.onError
import com.sorrowblue.comicviewer.framework.onSuccess
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

/**
 * サーバに接続して、接続できたら登録/更新する
 *
 * @property bookshelfRepository
 */
internal class RegisterBookshelfInteractor @Inject constructor(
    private val bookshelfRepository: BookshelfRepository
) : RegisterBookshelfUseCase() {

    override fun run(request: Request): Flow<Resource<Bookshelf, Error>> {
        return flow {
            bookshelfRepository.connect(request.bookshelf, request.path).first().onError {
                emit(
                    Resource.Error(
                        when (it) {
                            BookshelfRepository.Error.Network -> Error.Network
                            BookshelfRepository.Error.System -> Error.Auth
                            BookshelfRepository.Error.NotFound -> Error.Path
                        }
                    )
                )
            }
            val folder = Folder(request.bookshelf.id, request.bookshelf.displayName, "", request.path, 0, 0)
            val registerResource =
                bookshelfRepository.register(request.bookshelf, folder).first().fold({
                    Resource.Success(it)
                }, {
                    Resource.Error(
                        when (it) {
                            BookshelfRepository.Error.NotFound -> Error.Path
                            BookshelfRepository.Error.Network -> Error.Network
                            BookshelfRepository.Error.System -> Error.Auth
                        }
                    )
                })
            emit(registerResource)
        }
    }
}
