package com.sorrowblue.comicviewer.domain.repository

import com.sorrowblue.comicviewer.domain.model.settings.BookshelfSettings
import kotlinx.coroutines.flow.Flow

interface BookShelfSettingsRepository {

    val settingsFlow: Flow<BookshelfSettings>

    suspend fun update(transform: suspend (BookshelfSettings) -> BookshelfSettings)
    suspend fun update(settings: BookshelfSettings)
    suspend fun updateSort(sort: BookshelfSettings.Sort)
    suspend fun update(sort: BookshelfSettings.Sort, order: BookshelfSettings.Order)
}
