package com.sorrowblue.comicviewer.domain.model

import com.sorrowblue.comicviewer.domain.model.settings.BookshelfSettings

class UpdateBookshelfSettingsRequest(val update: (BookshelfSettings) -> BookshelfSettings) : BaseRequest {
    override fun validate(): Boolean {
        return true
    }
}
