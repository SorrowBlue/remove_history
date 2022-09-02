package com.sorrowblue.comicviewer.data.remote.di

import com.sorrowblue.comicviewer.data.datasource.BookRemoteDataSource
import com.sorrowblue.comicviewer.data.datasource.FileRemoteDataSource
import com.sorrowblue.comicviewer.data.remote.impl.BookRemoteDataSourceImpl
import com.sorrowblue.comicviewer.data.remote.impl.FileRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class SingletonBindsModule {

    @Binds
    abstract fun provideFileRemoteDataSource(factory: FileRemoteDataSourceImpl.Factory): FileRemoteDataSource.Factory

    @Binds
    abstract fun provideBookRemoteDataSource(factory: BookRemoteDataSourceImpl.Factory): BookRemoteDataSource.Factory
}
