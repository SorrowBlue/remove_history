package com.sorrowblue.comicviewer.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.sorrowblue.comicviewer.data.datastore.BookShelfSettingsSerializer
import com.sorrowblue.comicviewer.data.datastore.HistorySerializer
import com.sorrowblue.comicviewer.data.datastore.SettingsSerializer
import com.sorrowblue.comicviewer.domain.model.settings.BookshelfSettings
import com.sorrowblue.comicviewer.domain.model.History
import com.sorrowblue.comicviewer.domain.model.settings.Settings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
internal object SingletonProvidesModule {

    val Context.historyDataStore: DataStore<History> by dataStore(
        fileName = "history.pb",
        serializer = HistorySerializer
    )

    val Context.bookshelfSettingsDataStore: DataStore<BookshelfSettings> by dataStore(
        fileName = "bookShelfSettings.pb",
        serializer = BookShelfSettingsSerializer
    )

    val Context.settingsDataStore: DataStore<Settings> by dataStore(
        fileName = "settings.pb",
        serializer = SettingsSerializer
    )

    @Singleton
    @Provides
    fun provideHistoryDataStore(@ApplicationContext context: Context): DataStore<History> =
        context.historyDataStore

    @IoDispatcher
    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @DefaultDispatcher
    @Provides
    fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Singleton
    @Provides
    fun provideBookShelfSettingsDataStore(@ApplicationContext context: Context): DataStore<BookshelfSettings> =
        context.bookshelfSettingsDataStore

    @Singleton
    @Provides
    fun provideSettingsDataStore(@ApplicationContext context: Context): DataStore<Settings> =
        context.settingsDataStore
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IoDispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultDispatcher
