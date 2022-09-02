package com.sorrowblue.comicviewer.domain.model.file

import androidx.paging.PagingConfig
import com.sorrowblue.comicviewer.domain.model.BaseRequest
import com.sorrowblue.comicviewer.domain.model.library.Library

class LoadFileRequest(val pagingConfig: PagingConfig, val library: Library, val bookshelf: Bookshelf?) :
    BaseRequest {
    override fun validate(): Boolean {
        return true
    }
}
