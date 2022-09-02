package com.sorrowblue.comicviewer.data.database.di

import com.sorrowblue.comicviewer.data.database.impl.FileLocalDataSourceImpl
import com.sorrowblue.comicviewer.data.database.impl.LibraryLocalDataSourceImpl
import com.sorrowblue.comicviewer.data.datasource.FileLocalDataSource
import com.sorrowblue.comicviewer.data.datasource.LibraryLocalDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class SingletonBindsModule {

    @Singleton
    @Binds
    abstract fun bindFileLocalDataSource(dataSource: FileLocalDataSourceImpl): FileLocalDataSource

    @Singleton
    @Binds
    abstract fun bindLibraryLocalDataSource(dataSource: LibraryLocalDataSourceImpl): LibraryLocalDataSource
}
