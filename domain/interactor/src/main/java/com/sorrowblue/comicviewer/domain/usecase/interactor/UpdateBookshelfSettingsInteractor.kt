package com.sorrowblue.comicviewer.domain.usecase.interactor

import com.sorrowblue.comicviewer.domain.model.Response
import com.sorrowblue.comicviewer.domain.model.UpdateBookshelfSettingsRequest
import com.sorrowblue.comicviewer.domain.repository.BookShelfSettingsRepository
import com.sorrowblue.comicviewer.domain.usecase.UpdateBookshelfSettingsUseCase
import javax.inject.Inject

internal class UpdateBookshelfSettingsInteractor @Inject constructor(
    private val bookShelfSettingsRepository: BookShelfSettingsRepository,
) : UpdateBookshelfSettingsUseCase() {
    override suspend fun run(request: UpdateBookshelfSettingsRequest): Response<Unit> {
        bookShelfSettingsRepository.update {
            request.update.invoke(it)
        }
        return Response.Success(Unit)
    }
}
