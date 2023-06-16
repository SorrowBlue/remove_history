package com.sorrowblue.comicviewer.domain.usecase.favorite

import com.sorrowblue.comicviewer.domain.entity.favorite.FavoriteFile
import com.sorrowblue.comicviewer.domain.entity.file.Book
import com.sorrowblue.comicviewer.domain.request.BaseRequest
import com.sorrowblue.comicviewer.domain.usecase.FlowUseCase2
import com.sorrowblue.comicviewer.domain.usecase.GetLibraryInfoError
import com.sorrowblue.comicviewer.domain.usecase.GetNextComicRel

abstract class GetNextFavoriteBookUseCase :
    FlowUseCase2<GetNextFavoriteBookUseCase.Request, Book, GetLibraryInfoError>() {

    class Request(val favoriteFile: FavoriteFile, val relation: GetNextComicRel) :
        BaseRequest {
        override fun validate(): Boolean {
            return true
        }
    }
}
