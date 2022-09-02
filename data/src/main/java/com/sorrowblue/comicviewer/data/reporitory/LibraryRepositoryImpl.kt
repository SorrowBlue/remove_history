package com.sorrowblue.comicviewer.data.reporitory

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.sorrowblue.comicviewer.data.entity.toData
import com.sorrowblue.comicviewer.data.datasource.FileLocalDataSource
import com.sorrowblue.comicviewer.data.datasource.LibraryLocalDataSource
import com.sorrowblue.comicviewer.domain.model.Response
import com.sorrowblue.comicviewer.domain.model.library.Library
import com.sorrowblue.comicviewer.domain.repository.LibraryRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

internal class LibraryRepositoryImpl @Inject constructor(
    private val localDataSource: LibraryLocalDataSource,
    private val fileLocalDataSource: FileLocalDataSource,
) : LibraryRepository {

    override fun pagingDataFlow(pagingConfig: PagingConfig): Response.Success<Flow<PagingData<Library>>> {
        val pager = Pager(pagingConfig) { localDataSource.pagingSource() }.flow.map { pagingData ->
            pagingData.map { it.toLibrary(fileLocalDataSource.previewFlow(it.id, 10).first()) }
        }
        return Response.Success(pager)
    }

    override suspend fun create(library: Library): Response<Library> {
        return kotlin.runCatching {
            localDataSource.create(library.toData())
        }.fold({
            Response.Success(it.toLibrary())
        }, {
            Response.Error(it)
        })
    }
}
