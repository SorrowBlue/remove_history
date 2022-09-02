package com.sorrowblue.comicviewer.data.reporitory

import androidx.datastore.core.DataStore
import com.sorrowblue.comicviewer.domain.model.settings.Settings
import com.sorrowblue.comicviewer.domain.repository.SettingsRepository
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(private val dataStore: DataStore<Settings>) : SettingsRepository {

    override val settingsFlow = dataStore.data

    override suspend fun update(action: (Settings) -> Settings) {
        dataStore.updateData { action(it) }
    }
}
