package com.sorrowblue.comicviewer.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sorrowblue.comicviewer.data.database.dao.FileDataDao
import com.sorrowblue.comicviewer.data.database.dao.LibraryDataDao
import com.sorrowblue.comicviewer.data.entity.FileData
import com.sorrowblue.comicviewer.data.entity.LibraryData
import com.sorrowblue.comicviewer.data.entity.LocalDateTimeConverters

@Database(entities = [LibraryData::class, FileData::class], version = 1)
@TypeConverters(LocalDateTimeConverters::class)
internal abstract class ComicViewerDatabase : RoomDatabase() {

    abstract fun libraryDataDao(): LibraryDataDao

    abstract fun fileDataDao(): FileDataDao
}
