package com.sorrowblue.comicviewer.data.reporitory

import androidx.datastore.core.DataStore
import com.sorrowblue.comicviewer.domain.model.settings.BookshelfSettings
import com.sorrowblue.comicviewer.domain.model.History
import com.sorrowblue.comicviewer.domain.repository.BookShelfSettingsRepository
import com.sorrowblue.comicviewer.domain.repository.HistoryRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

internal class HistoryRepositoryImpl @Inject constructor(private val dataStore: DataStore<History>) :
    HistoryRepository {

    override val history = dataStore.data

    override suspend fun update(history: History) {
        dataStore.updateData {
            it.copy(history.bookshelfId, history.currentComic)
        }
    }
}

internal class BookShelfSettingsRepositoryImpl @Inject constructor(private val dataStore: DataStore<BookshelfSettings>) :
    BookShelfSettingsRepository {

    override val settingsFlow: Flow<BookshelfSettings> = dataStore.data

    override suspend fun update(transform: suspend (BookshelfSettings) -> BookshelfSettings) {
        dataStore.updateData(transform::invoke)
    }

    override suspend fun update(settings: BookshelfSettings) {
        dataStore.updateData { settings }
    }

    override suspend fun update(sort: BookshelfSettings.Sort, order: BookshelfSettings.Order) {
        dataStore.updateData { it.copy(sort = sort, order = order) }
    }

    override suspend fun updateSort(sort: BookshelfSettings.Sort) {
        dataStore.updateData {
            it.copy(sort = sort)
        }

    }
}
