package com.sorrowblue.comicviewer.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.sorrowblue.comicviewer.data.database.entity.BookshelfEntity
import com.sorrowblue.comicviewer.data.database.entity.BookshelfFileCountEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface BookshelfDao {

    @Upsert
    suspend fun upsert(entity: BookshelfEntity): Long

    @Delete
    suspend fun delete(entity: BookshelfEntity): Int

    @Query("SELECT * FROM bookshelf WHERE id = :bookshelfId")
    fun flow(bookshelfId: Int): Flow<BookshelfEntity?>

    @Query(
        "SELECT bookshelf.*, file.*, (SELECT COUNT(*) FROM file file2 WHERE bookshelf.id = file2.bookshelf_id AND file2.file_type = 'FILE') file_count FROM bookshelf LEFT OUTER JOIN file ON bookshelf.id = file.bookshelf_id AND file.parent = '' ORDER BY bookshelf.id"
    )
    fun pagingSource(): PagingSource<Int, BookshelfFileCountEntity>
}
