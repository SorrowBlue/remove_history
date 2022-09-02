package com.sorrowblue.comicviewer.domain.usecase.interactor

import com.sorrowblue.comicviewer.domain.model.Response
import com.sorrowblue.comicviewer.domain.model.settings.Settings
import com.sorrowblue.comicviewer.domain.model.settings.UpdateSettingsRequest
import com.sorrowblue.comicviewer.domain.repository.SettingsRepository
import com.sorrowblue.comicviewer.domain.usecase.LoadSettingsUseCase
import com.sorrowblue.comicviewer.domain.usecase.UpdateSettingsUseCase
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

internal class LoadSettingsInteractor @Inject constructor(
    private val repository: SettingsRepository,
) : LoadSettingsUseCase() {
    override fun execute(): Flow<Settings> {
        return repository.settingsFlow
    }
}

internal class UpdateSettingsInteractor @Inject constructor(
    private val repository: SettingsRepository,
) : UpdateSettingsUseCase() {
    override suspend fun run(request: UpdateSettingsRequest): Response<Unit> {
        repository.update {
            it.copy(request.useAuth ?: it.useAuth)
        }
        return Response.Success(Unit)
    }
}
