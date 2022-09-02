package com.sorrowblue.comicviewer.domain.model

import com.sorrowblue.comicviewer.domain.model.BaseRequest
import com.sorrowblue.comicviewer.domain.model.file.Book
import com.sorrowblue.comicviewer.domain.model.library.Library

class LoadPageRequest(val library: Library, val book: Book, val index: Int) : BaseRequest {
    override fun validate(): Boolean {
        return true
    }
}
