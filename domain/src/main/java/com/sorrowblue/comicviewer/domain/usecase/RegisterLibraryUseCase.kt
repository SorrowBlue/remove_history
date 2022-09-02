package com.sorrowblue.comicviewer.domain.usecase

import com.sorrowblue.comicviewer.domain.model.library.Library
import com.sorrowblue.comicviewer.domain.model.library.RegisterLibraryRequest

abstract class RegisterLibraryUseCase : MultipleUseCase<RegisterLibraryRequest, Library>()
