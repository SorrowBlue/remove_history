package com.sorrowblue.comicviewer.domain.usecase.interactor

import com.sorrowblue.comicviewer.domain.model.Response
import com.sorrowblue.comicviewer.domain.model.library.Library
import com.sorrowblue.comicviewer.domain.model.library.RegisterLibraryRequest
import com.sorrowblue.comicviewer.domain.repository.LibraryRepository
import com.sorrowblue.comicviewer.domain.usecase.RegisterLibraryUseCase
import javax.inject.Inject

internal class RegisterLibraryInteractor @Inject constructor(
    private val repository: LibraryRepository,
) : RegisterLibraryUseCase() {

    override suspend fun run(request: RegisterLibraryRequest): Response<Library> {
        return repository.create(request.library)
    }
}
