package com.sorrowblue.comicviewer.domain.repository

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.sorrowblue.comicviewer.domain.model.Response
import com.sorrowblue.comicviewer.domain.model.file.Bookshelf
import com.sorrowblue.comicviewer.domain.model.file.File
import com.sorrowblue.comicviewer.domain.model.library.Library
import kotlinx.coroutines.flow.Flow

interface FileRepository {

    fun pagingDataFlow(
        pagingConfig: PagingConfig,
        library: Library,
        bookshelf: Bookshelf?,
    ): Response.Success<Flow<PagingData<File>>>
}

