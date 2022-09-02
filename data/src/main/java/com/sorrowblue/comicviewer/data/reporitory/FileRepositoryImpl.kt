package com.sorrowblue.comicviewer.data.reporitory

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.sorrowblue.comicviewer.data.datasource.FileLocalDataSource
import com.sorrowblue.comicviewer.data.entity.toData
import com.sorrowblue.comicviewer.domain.model.Response
import com.sorrowblue.comicviewer.domain.model.file.Bookshelf
import com.sorrowblue.comicviewer.domain.model.file.File
import com.sorrowblue.comicviewer.domain.model.library.Library
import com.sorrowblue.comicviewer.domain.repository.BookShelfSettingsRepository
import com.sorrowblue.comicviewer.domain.repository.FileRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

internal class FileRepositoryImpl @Inject constructor(
    private val bookShelfSettingsRepository: BookShelfSettingsRepository,
    private val factory: FileRemoteMediator.Factory,
    private val fileLocalDataSource: FileLocalDataSource,
) : FileRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun pagingDataFlow(
        pagingConfig: PagingConfig,
        library: Library,
        bookshelf: Bookshelf?,
    ): Response.Success<Flow<PagingData<File>>> {
        val settings = runBlocking { bookShelfSettingsRepository.settingsFlow.first() }
        val remoteMediator = factory.create(library.toData(), bookshelf?.toData(library.id))
        val data = Pager(pagingConfig, remoteMediator = remoteMediator) {
            fileLocalDataSource.pagingSource(library.id.value,
                bookshelf?.path ?: library.path,
                settings)
        }.flow.map { pagingData ->
            pagingData.map {
                it.toFile(fileLocalDataSource.previewFlow(it.libraryId, it.parent, 5))
            }
        }
        return Response.Success(data)
    }
}

