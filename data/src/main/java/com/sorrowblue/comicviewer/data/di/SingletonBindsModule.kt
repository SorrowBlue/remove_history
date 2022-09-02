package com.sorrowblue.comicviewer.data.di

import com.sorrowblue.comicviewer.data.reporitory.BookRepositoryImpl
import com.sorrowblue.comicviewer.data.reporitory.BookShelfSettingsRepositoryImpl
import com.sorrowblue.comicviewer.data.reporitory.HistoryRepositoryImpl
import com.sorrowblue.comicviewer.data.reporitory.SettingsRepositoryImpl
import com.sorrowblue.comicviewer.domain.repository.BookRepository
import com.sorrowblue.comicviewer.domain.repository.BookShelfSettingsRepository
import com.sorrowblue.comicviewer.domain.repository.HistoryRepository
import com.sorrowblue.comicviewer.domain.repository.SettingsRepository
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
    abstract fun bindHistoryRepository(repository: HistoryRepositoryImpl): HistoryRepository

    @Singleton
    @Binds
    abstract fun bindBookShelfSettingsRepository(repository: BookShelfSettingsRepositoryImpl): BookShelfSettingsRepository

    @Singleton
    @Binds
    abstract fun bindSettingsRepository(repository: SettingsRepositoryImpl): SettingsRepository

    @Singleton
    @Binds
    abstract fun bindBookRepositoryFactory(factory: BookRepositoryImpl.Factory): BookRepository.Factory
}
