package com.sorrowblue.comicviewer.domain.repository

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.sorrowblue.comicviewer.domain.model.library.Library
import com.sorrowblue.comicviewer.domain.model.Response
import kotlinx.coroutines.flow.Flow

interface LibraryRepository {

    fun pagingDataFlow(pagingConfig: PagingConfig): Response.Success<Flow<PagingData<Library>>>
    suspend fun create(library: Library): Response<Library>
}

