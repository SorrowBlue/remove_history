package com.sorrowblue.comicviewer.domain.service.interactor.bookshelf

import com.sorrowblue.comicviewer.domain.model.BookshelfFolder
import com.sorrowblue.comicviewer.domain.model.Resource
import com.sorrowblue.comicviewer.domain.service.datasource.BookshelfLocalDataSource
import com.sorrowblue.comicviewer.domain.service.datasource.FileLocalDataSource
import com.sorrowblue.comicviewer.domain.usecase.bookshelf.GetBookshelfInfoUseCase
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class GetBookshelfInfoInteractor @Inject constructor(
    private val localBookshelfLocalDataSource: BookshelfLocalDataSource,
    private val fileLocalDataSource: FileLocalDataSource,
) : GetBookshelfInfoUseCase() {

    override fun run(request: Request): Flow<Resource<BookshelfFolder, Error>> {
        return localBookshelfLocalDataSource.flow(request.bookshelfId).map { bookshelf ->
            if (bookshelf != null) {
                val folder = fileLocalDataSource.root(request.bookshelfId)
                if (folder != null) {
                    Resource.Success(BookshelfFolder(bookshelf, folder))
                } else {
                    Resource.Error(Error.NotFound)
                }
            } else {
                Resource.Error(Error.NotFound)
            }
        }
    }
}
