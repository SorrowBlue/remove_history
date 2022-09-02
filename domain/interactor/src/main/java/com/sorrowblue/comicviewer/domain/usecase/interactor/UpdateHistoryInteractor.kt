package com.sorrowblue.comicviewer.domain.usecase.interactor

import com.sorrowblue.comicviewer.domain.model.Response
import com.sorrowblue.comicviewer.domain.model.UpdateHistoryRequest
import com.sorrowblue.comicviewer.domain.repository.HistoryRepository
import com.sorrowblue.comicviewer.domain.usecase.UpdateHistoryUseCase
import javax.inject.Inject

internal class UpdateHistoryInteractor @Inject constructor(
    private val historyRepository: HistoryRepository,
) : UpdateHistoryUseCase() {
    override suspend fun run(request: UpdateHistoryRequest): Response<Unit> {
        historyRepository.update(request.history)
        return Response.Success(Unit)
    }
}
