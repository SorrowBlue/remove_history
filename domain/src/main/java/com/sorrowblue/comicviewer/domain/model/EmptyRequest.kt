package com.sorrowblue.comicviewer.domain.model

object EmptyRequest : BaseRequest {
    override fun validate(): Boolean {
        return true
    }
}
