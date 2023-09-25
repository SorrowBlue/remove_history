package com.sorrowblue.comicviewer.data.storage.device

import com.sorrowblue.comicviewer.data.storage.client.FileClient
import com.sorrowblue.comicviewer.data.storage.client.qualifier.DeviceFileClientFactory
import com.sorrowblue.comicviewer.domain.model.bookshelf.InternalStorage
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class SingletonBindsModule {

    @DeviceFileClientFactory
    @Binds
    abstract fun bindDeviceFileClientFactory(factory: DeviceFileClient.Factory): FileClient.Factory<InternalStorage>
}
