package com.sorrowblue.comicviewer.domain.usecase

import com.sorrowblue.comicviewer.domain.model.settings.Settings
import com.sorrowblue.comicviewer.domain.model.settings.UpdateSettingsRequest
import kotlinx.coroutines.flow.Flow

abstract class IsSettingsUseCase : SafeUseCase<Flow<Settings>>
abstract class LoadSettingsUseCase : SafeUseCase<Flow<Settings>>
abstract class UpdateSettingsUseCase : MultipleUseCase<UpdateSettingsRequest, Unit>()

interface SafeUseCase<T> {

    fun execute(): T
}
