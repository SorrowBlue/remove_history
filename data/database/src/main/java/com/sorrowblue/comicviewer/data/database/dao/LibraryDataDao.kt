package com.sorrowblue.comicviewer.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sorrowblue.comicviewer.data.entity.LibraryData
import kotlinx.coroutines.flow.Flow

@Dao
internal interface LibraryDataDao {

    @Query("SELECT * FROM library_data ORDER BY id")
    fun pagingSource(): PagingSource<Int, LibraryData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: LibraryData): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(data: LibraryData): Int

    @Delete
    suspend fun delete(data: LibraryData)

    @Query("SELECT * FROM library_data ORDER BY id")
    fun all(): Flow<List<LibraryData>>
}
