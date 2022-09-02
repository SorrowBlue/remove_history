package com.sorrowblue.comicviewer.data.di

import com.sorrowblue.comicviewer.data.reporitory.FileRepositoryImpl
import com.sorrowblue.comicviewer.data.reporitory.LibraryRepositoryImpl
import com.sorrowblue.comicviewer.domain.repository.FileRepository
import com.sorrowblue.comicviewer.domain.repository.LibraryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
internal abstract class ViewModelBindsModule {

    @Binds
    abstract fun bindLibraryRepository(repository: LibraryRepositoryImpl): LibraryRepository

    @Binds
    abstract fun bindFileRepository(repository: FileRepositoryImpl): FileRepository
}
