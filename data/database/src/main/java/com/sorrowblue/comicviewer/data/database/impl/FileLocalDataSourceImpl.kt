package com.sorrowblue.comicviewer.data.database.impl

import androidx.paging.PagingSource
import com.sorrowblue.comicviewer.data.database.dao.FileDataDao
import com.sorrowblue.comicviewer.data.database.dao.pagingSource
import com.sorrowblue.comicviewer.data.entity.FileData
import com.sorrowblue.comicviewer.data.entity.LibraryData
import com.sorrowblue.comicviewer.data.datasource.FileLocalDataSource
import com.sorrowblue.comicviewer.domain.model.settings.BookshelfSettings
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

internal class FileLocalDataSourceImpl @Inject constructor(private val dao: FileDataDao) :
    FileLocalDataSource {

    override suspend fun previewFlow(libraryId: Int, limit: Int): Flow<List<String>> {
        return dao.findPreview(libraryId, limit)
    }

    override suspend fun previewFlow(libraryId: Int, parent: String, limit: Int): List<String> {
        return dao.findPreview(libraryId, parent, limit)
    }

    override suspend fun update(fileData: FileData) {
        dao.update(fileData)
    }

    override fun pagingSource(libraryId: Int, parent: String, settings: BookshelfSettings): PagingSource<Int, FileData> {
        return dao.pagingSource(libraryId, parent, settings)
    }

    override suspend fun deleteIfNotFound(libraryData: LibraryData, newFiles: List<FileData>) {
        dao.deleteIfNotFound(libraryData.id,
            newFiles.first().parent,
            newFiles.map(FileData::path))
    }

    override suspend fun upsert(fileData: List<FileData>) {
        val list = fileData.map { newData ->
            dao.selectBy(newData.libraryId, newData.path)
                ?.copy(timestamp = newData.timestamp,
                    fileSize = newData.fileSize,
                    preview = newData.preview) ?: newData
        }
        dao.upsert(list)
    }

    override suspend fun findUpdatePreview(library: LibraryData, newData: FileData): FileData? {
        return dao.findUpdatePreview(library.id, newData.path, newData.timestamp)
    }
}
