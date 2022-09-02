package com.sorrowblue.comicviewer.data.datasource

import com.sorrowblue.comicviewer.data.entity.FileData
import com.sorrowblue.comicviewer.data.entity.LibraryData
import java.io.InputStream

interface BookRemoteDataSource {

    interface Factory {
        fun create(libraryData: LibraryData, fileData: FileData): BookRemoteDataSource
    }

    fun count(): Int
    fun pageInputStream(pageIndex: Int): InputStream
    fun close()
}
