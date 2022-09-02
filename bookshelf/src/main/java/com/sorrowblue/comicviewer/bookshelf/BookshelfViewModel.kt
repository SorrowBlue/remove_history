package com.sorrowblue.comicviewer.bookshelf

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.sorrowblue.comicviewer.domain.model.EmptyRequest
import com.sorrowblue.comicviewer.domain.model.Response
import com.sorrowblue.comicviewer.domain.model.file.LoadFileRequest
import com.sorrowblue.comicviewer.domain.usecase.GetBookshelfSettingsUseCase
import com.sorrowblue.comicviewer.domain.usecase.LoadFileUseCase
import com.sorrowblue.comicviewer.framework.ui.navigation.SupportSafeArgs
import com.sorrowblue.comicviewer.framework.ui.navigation.navArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.runBlocking

@HiltViewModel
class BookshelfViewModel @Inject constructor(
    loadFileUseCase: LoadFileUseCase,
    private val getBookshelfSettingsUseCase: GetBookshelfSettingsUseCase,
    override val savedStateHandle: SavedStateHandle,
) : ViewModel(), SupportSafeArgs {

    private val args: BookshelfFragmentArgs by navArgs()
    val library = args.library

    val settings2 = runBlocking {
        getBookshelfSettingsUseCase.execute(EmptyRequest)
            .let { it as Response.Success }.data.first()
    }
    val settings = getBookshelfSettingsUseCase.execute(EmptyRequest)
        .let { it as Response.Success }.data.shareIn(viewModelScope, SharingStarted.Eagerly)

    val title = library.name.ifEmpty { "${library.host}/${library.path}" }
    val subTitle = args.bookshelf?.name.orEmpty()

    val data = (loadFileUseCase.execute(
        LoadFileRequest(PagingConfig(100), args.library, args.bookshelf)
    ) as Response.Success).data
        .cachedIn(viewModelScope)
}

