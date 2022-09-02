package com.sorrowblue.comicviewer.domain.repository

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.sorrowblue.comicviewer.domain.model.Response
import com.sorrowblue.comicviewer.domain.model.library.Library
import com.sorrowblue.comicviewer.domain.model.file.Book
import com.sorrowblue.comicviewer.domain.model.page.Page
import kotlinx.coroutines.flow.Flow

interface BookRepository {

    interface Factory {
        fun create(library: Library, book: Book): BookRepository
    }

    suspend fun clearCache()

    suspend fun pageCount(): Int
    suspend fun loadPage(page: Int):String
    suspend fun close()
}
