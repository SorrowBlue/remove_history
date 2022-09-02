package com.sorrowblue.comicviewer.data.datasource

import androidx.paging.PagingSource
import com.sorrowblue.comicviewer.data.entity.LibraryData

interface LibraryLocalDataSource {

    fun pagingSource(): PagingSource<Int, LibraryData>

    suspend fun create(library: LibraryData): LibraryData
}
