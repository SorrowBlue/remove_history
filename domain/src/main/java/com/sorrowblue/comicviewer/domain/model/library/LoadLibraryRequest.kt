package com.sorrowblue.comicviewer.domain.model.library

import androidx.paging.PagingConfig
import com.sorrowblue.comicviewer.domain.model.BaseRequest

class LoadLibraryRequest(val pagingConfig: PagingConfig) : BaseRequest {
    override fun validate(): Boolean {
        return true
    }
}
