package com.sorrowblue.comicviewer.domain.usecase

import com.sorrowblue.comicviewer.domain.model.UpdateBookshelfSettingsRequest

abstract class UpdateBookshelfSettingsUseCase :
    MultipleUseCase<UpdateBookshelfSettingsRequest, Unit>()
