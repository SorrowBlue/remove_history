package com.sorrowblue.comicviewer.domain.usecase

import com.sorrowblue.comicviewer.domain.model.settings.BookshelfSettings
import com.sorrowblue.comicviewer.domain.model.EmptyRequest
import kotlinx.coroutines.flow.Flow

abstract class GetBookshelfSettingsUseCase : OneTimeUseCase<EmptyRequest, Flow<BookshelfSettings>>()
