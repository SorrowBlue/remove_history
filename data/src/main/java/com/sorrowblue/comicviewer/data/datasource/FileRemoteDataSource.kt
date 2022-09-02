package com.sorrowblue.comicviewer.data.datasource

import com.sorrowblue.comicviewer.data.entity.FileData
import com.sorrowblue.comicviewer.data.entity.LibraryData

interface FileRemoteDataSource {

    interface Factory {
        fun create(libraryData: LibraryData): FileRemoteDataSource
    }

    suspend fun listFiles(fileData: FileData?): List<FileData>

    suspend fun getPreview(list: List<FileData>, done: suspend (FileData) -> Unit)
}

