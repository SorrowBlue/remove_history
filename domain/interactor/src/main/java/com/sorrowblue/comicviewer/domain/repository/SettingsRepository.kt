package com.sorrowblue.comicviewer.domain.repository

import com.sorrowblue.comicviewer.domain.model.settings.Settings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val settingsFlow: Flow<Settings>
    suspend fun update(action: (Settings) -> Settings)
}
