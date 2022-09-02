package com.sorrowblue.comicviewer.data.datasource

import androidx.paging.PagingSource
import com.sorrowblue.comicviewer.data.entity.FileData
import com.sorrowblue.comicviewer.data.entity.LibraryData
import com.sorrowblue.comicviewer.domain.model.settings.BookshelfSettings
import kotlinx.coroutines.flow.Flow

interface FileLocalDataSource {

    suspend fun previewFlow(libraryId: Int, limit: Int): Flow<List<String>>
    suspend fun deleteIfNotFound(libraryData: LibraryData, newFiles: List<FileData>)
    suspend fun upsert(fileData: List<FileData>)
    suspend fun findUpdatePreview(library: LibraryData, newData: FileData): FileData?
    suspend fun update(fileData: FileData)
    fun pagingSource(libraryId: Int, parent: String, settings: BookshelfSettings): PagingSource<Int, FileData>
    suspend fun previewFlow(libraryId: Int, parent: String, limit: Int): List<String>
}
