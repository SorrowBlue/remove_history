package com.sorrowblue.comicviewer.domain.model

class UpdateHistoryRequest(val history: History) : BaseRequest {
    override fun validate(): Boolean {
        return true
    }
}
