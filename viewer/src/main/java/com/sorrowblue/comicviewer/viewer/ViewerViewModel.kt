package com.sorrowblue.comicviewer.viewer

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sorrowblue.comicviewer.domain.model.History
import com.sorrowblue.comicviewer.domain.model.LoadPageRequest
import com.sorrowblue.comicviewer.domain.model.Response
import com.sorrowblue.comicviewer.domain.model.UpdateHistoryRequest
import com.sorrowblue.comicviewer.domain.model.page.Page
import com.sorrowblue.comicviewer.domain.usecase.LoadPageUseCase
import com.sorrowblue.comicviewer.domain.usecase.UpdateHistoryUseCase
import com.sorrowblue.comicviewer.framework.ui.navigation.SupportSafeArgs
import com.sorrowblue.comicviewer.framework.ui.navigation.navArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
internal class ViewerViewModel @Inject constructor(
    private val updateHistoryUseCase: UpdateHistoryUseCase,
    private val loadPageUseCase: LoadPageUseCase,
    override val savedStateHandle: SavedStateHandle,
) : ViewModel(), SupportSafeArgs {

    val isVisibleUI = MutableStateFlow(false)
    private val args: ViewerFragmentArgs by navArgs()
    val book = args.book
    val max = args.book.maxPage

    val position = MutableStateFlow(0)

    val title = args.book.name

    fun next() {
        if (position.value + 1 < max - 1) {
            position.value = position.value + 1
        }
    }

    fun back() {
        if (0 <= position.value - 1) {
            position.value = position.value - 1
        }
    }

    init {
        viewModelScope.launch {
            updateHistoryUseCase.execute(UpdateHistoryRequest(History(args.library.id.value,
                args.book.parent)))
        }
    }

    override fun onCleared() {
        viewModelScope.launch {
            loadPageUseCase.clear()
        }
        super.onCleared()
    }

    suspend fun loadPage(page: Int): Page {
        return (loadPageUseCase.execute(LoadPageRequest(args.library,
            args.book,
            page)) as Response.Success).data
    }
}

