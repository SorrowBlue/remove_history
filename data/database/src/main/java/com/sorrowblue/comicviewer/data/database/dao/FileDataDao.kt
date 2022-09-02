package com.sorrowblue.comicviewer.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQueryBuilder
import com.sorrowblue.comicviewer.data.entity.FileData
import com.sorrowblue.comicviewer.domain.model.settings.BookshelfSettings
import java.time.LocalDateTime
import kotlinx.coroutines.flow.Flow

@Dao
internal interface FileDataDao {

    @RawQuery(observedEntities = [FileData::class])
    fun pagingSource(query: SupportSQLiteQuery): PagingSource<Int, FileData>

    @Query("SELECT preview FROM file WHERE library_id = :libraryId AND parent = :parent AND preview NOT null LIMIT :limit")
    suspend fun findPreview(libraryId: Int, parent: String, limit: Int): List<String>

    @Query("SELECT preview FROM file WHERE library_id = :libraryId AND preview NOT null LIMIT :limit")
    fun findPreview(libraryId: Int, limit: Int): Flow<List<String>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(fileData: FileData): Int

    @Delete
    suspend fun delete(fileData: FileData)

    @Query("SELECT * FROM file WHERE library_id = :libraryId AND path = :path AND is_file = 1 AND (timestamp != :timestamp OR preview = '' OR preview IS NULL)")
    suspend fun findUpdatePreview(
        libraryId: Int,
        path: String,
        timestamp: LocalDateTime
    ): FileData?

    @Query("DELETE FROM file WHERE library_id = :libraryId AND parent = :parent AND path NOT IN (:paths)")
    suspend fun deleteIfNotFound(libraryId: Int, parent: String, paths: List<String>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(fileData: List<FileData>)

    @Query("SELECT * FROM file WHERE library_id = :libraryId AND path = :path")
    suspend fun selectBy(libraryId: Int, path: String): FileData?
}

internal fun FileDataDao.pagingSource(libraryId: Int, parent: String, settings: BookshelfSettings): PagingSource<Int, FileData> {
    val query = SupportSQLiteQueryBuilder.builder("file").apply {
        selection("library_id = :libraryId AND parent = :parent", arrayOf(libraryId, parent))
        val orderByStr = when (settings.sort) {
            BookshelfSettings.Sort.NAME -> when (settings.order) {
                BookshelfSettings.Order.ASC -> "is_file, name"
                BookshelfSettings.Order.DESC -> "is_file DESC, name DESC"
            }
            BookshelfSettings.Sort.DATE -> when(settings.order) {
                BookshelfSettings.Order.ASC -> "is_file,timestamp,name"
                BookshelfSettings.Order.DESC -> "is_file DESC, timestamp DESC, name DESC"
            }
            BookshelfSettings.Sort.SIZE -> when(settings.order) {
                BookshelfSettings.Order.ASC -> "is_file,size,name"
                BookshelfSettings.Order.DESC -> "is_file DESC, size DESC, name DESC"
            }
        }
        orderBy(orderByStr)
    }.create()
    return pagingSource(query)
}
