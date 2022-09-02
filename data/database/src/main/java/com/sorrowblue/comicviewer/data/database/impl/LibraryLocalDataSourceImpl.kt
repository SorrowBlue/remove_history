package com.sorrowblue.comicviewer.data.database.impl

import androidx.paging.PagingSource
import com.sorrowblue.comicviewer.data.database.dao.FileDataDao
import com.sorrowblue.comicviewer.data.database.dao.LibraryDataDao
import com.sorrowblue.comicviewer.data.entity.LibraryData
import com.sorrowblue.comicviewer.data.datasource.LibraryLocalDataSource
import javax.inject.Inject

internal class LibraryLocalDataSourceImpl @Inject constructor(
    private val dao: LibraryDataDao,
    private val fileDataDao: FileDataDao,
) : LibraryLocalDataSource {
    override fun pagingSource(): PagingSource<Int, LibraryData> {
        return dao.pagingSource()
    }

    override suspend fun create(library: LibraryData): LibraryData {
        return dao.insert(library).let {
            library.copy(it.toInt())
        }
    }
}

