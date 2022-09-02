package com.sorrowblue.comicviewer.data.reporitory

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.sorrowblue.comicviewer.data.datasource.FileLocalDataSource
import com.sorrowblue.comicviewer.data.datasource.FileRemoteDataSource
import com.sorrowblue.comicviewer.data.di.IoDispatcher
import com.sorrowblue.comicviewer.data.entity.FileData
import com.sorrowblue.comicviewer.data.entity.LibraryData
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal fun FileData.resolvePreview(context: Context): FileData {
    return if (isFile) {
        val preview = context.cacheDir.resolve("covers").resolve(previewName)
        if (preview.exists()) copy(preview = preview.toString()) else this
    } else {
        this
    }
}

@OptIn(ExperimentalPagingApi::class)
internal class FileRemoteMediator @AssistedInject constructor(
    factory: FileRemoteDataSource.Factory,
    @Assisted private val libraryData: LibraryData,
    @Assisted private val fileData: FileData?,
    @ApplicationContext private val context: Context,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val localDataSource: FileLocalDataSource,
) : RemoteMediator<Int, FileData>() {

    @AssistedFactory
    interface Factory {
        fun create(libraryData: LibraryData, fileData: FileData?): FileRemoteMediator
    }

    private val remoteDataSource = factory.create(libraryData)

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, FileData>,
    ): MediatorResult {
        withContext(dispatcher) {
            val files = remoteDataSource.listFiles(fileData).map { it.resolvePreview(context) }
            if (files.isNotEmpty()) {
                localDataSource.deleteIfNotFound(libraryData, files)
                localDataSource.upsert(files)
                files.mapNotNull {
                    localDataSource.findUpdatePreview(libraryData, it)
                }.let {
                    remoteDataSource.getPreview(it, localDataSource::update)
                }
            }
        }
        return MediatorResult.Success(endOfPaginationReached = true)
    }
}

