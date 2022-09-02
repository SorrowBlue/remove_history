package com.sorrowblue.comicviewer.domain.repository

import com.sorrowblue.comicviewer.domain.model.History
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    val history: Flow<History>
    suspend fun update(history: History)
}

