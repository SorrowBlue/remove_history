package com.sorrowblue.comicviewer.domain.model.library

import com.sorrowblue.comicviewer.domain.model.BaseRequest

class RegisterLibraryRequest(val library: Library) : BaseRequest {
    override fun validate(): Boolean {
        return true
    }
}
