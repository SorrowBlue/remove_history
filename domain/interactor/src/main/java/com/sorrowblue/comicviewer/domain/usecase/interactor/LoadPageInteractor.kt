package com.sorrowblue.comicviewer.domain.usecase.interactor

import com.sorrowblue.comicviewer.domain.model.settings.BookshelfSettings
import com.sorrowblue.comicviewer.domain.model.EmptyRequest
import com.sorrowblue.comicviewer.domain.model.Response
import com.sorrowblue.comicviewer.domain.model.page.Page
import com.sorrowblue.comicviewer.domain.repository.BookRepository
import com.sorrowblue.comicviewer.domain.repository.BookShelfSettingsRepository
import com.sorrowblue.comicviewer.domain.usecase.GetBookshelfSettingsUseCase
import com.sorrowblue.comicviewer.domain.usecase.LoadPageUseCase
import com.sorrowblue.comicviewer.domain.model.LoadPageRequest
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

internal class LoadPageInteractor @Inject constructor(
    private val factory: BookRepository.Factory,
) : LoadPageUseCase() {

    private var _repository: BookRepository? = null

    fun repository(request: LoadPageRequest): BookRepository {
        return _repository ?: factory.create(request.library, request.book).also {
            _repository = it
        }
    }

    private lateinit var pages: MutableList<Page>

    override suspend fun run(request: LoadPageRequest): Response<Page> {
        if (!::pages.isInitialized) {
            pages = MutableList(request.book.maxPage) { Page(it, null) }
            repository(request).clearCache()
        }
        if (pages[request.index].preview == null) {
            withContext(Dispatchers.IO) {
                pages[request.index] =
                    pages[request.index].copy(preview =
                    repository(request).loadPage(request.index))
            }
        }
        return Response.Success(pages[request.index])
    }

    override suspend fun clear() {
        _repository?.close()
    }
}

internal class GetBookshelfSettingsInteractor @Inject constructor(
    private val repository: BookShelfSettingsRepository,
) : GetBookshelfSettingsUseCase() {
    override fun run(request: EmptyRequest): Response<Flow<BookshelfSettings>> {
        return Response.Success(repository.settingsFlow)
    }
}
