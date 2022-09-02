package com.sorrowblue.comicviewer.domain.usecase

import androidx.paging.PagingData
import com.sorrowblue.comicviewer.domain.model.file.File
import com.sorrowblue.comicviewer.domain.model.file.LoadFileRequest
import kotlinx.coroutines.flow.Flow

abstract class LoadFileUseCase : OneTimeUseCase<LoadFileRequest, Flow<PagingData<File>>>()
