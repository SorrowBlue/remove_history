package com.sorrowblue.comicviewer.domain.usecase

import androidx.paging.PagingData
import com.sorrowblue.comicviewer.domain.model.library.Library
import com.sorrowblue.comicviewer.domain.model.library.LoadLibraryRequest
import kotlinx.coroutines.flow.Flow

/**
 * 図書館(サーバ設定リストを取得する)
 */
abstract class LoadLibraryUseCase : OneTimeUseCase<LoadLibraryRequest, Flow<PagingData<Library>>>()
