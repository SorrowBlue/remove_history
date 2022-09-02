package com.sorrowblue.comicviewer.data.database.di

import android.content.Context
import androidx.room.Room
import com.sorrowblue.comicviewer.data.database.ComicViewerDatabase
import com.sorrowblue.comicviewer.data.database.dao.FileDataDao
import com.sorrowblue.comicviewer.data.database.dao.LibraryDataDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object SingletonProvidesModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): ComicViewerDatabase =
        Room.databaseBuilder(context, ComicViewerDatabase::class.java, "comic_viewer_database")
            .build()

    @Singleton
    @Provides
    fun provideBookshelfDao(db: ComicViewerDatabase): LibraryDataDao =
        db.libraryDataDao()

    @Singleton
    @Provides
    fun provideFileDataDao(db: ComicViewerDatabase): FileDataDao =
        db.fileDataDao()

}
