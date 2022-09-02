package com.sorrowblue.comicviewer.domain.usecase

import com.sorrowblue.comicviewer.domain.model.LoadPageRequest
import com.sorrowblue.comicviewer.domain.model.page.Page

abstract class LoadPageUseCase : SingleUseCase<LoadPageRequest, Page>(), ClearResource
