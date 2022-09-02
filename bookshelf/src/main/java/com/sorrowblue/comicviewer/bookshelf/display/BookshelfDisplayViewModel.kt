package com.sorrowblue.comicviewer.bookshelf.display

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sorrowblue.comicviewer.domain.model.Display
import com.sorrowblue.comicviewer.domain.model.EmptyRequest
import com.sorrowblue.comicviewer.domain.model.Response
import com.sorrowblue.comicviewer.domain.model.UpdateBookshelfSettingsRequest
import com.sorrowblue.comicviewer.domain.model.settings.BookshelfSettings
import com.sorrowblue.comicviewer.domain.usecase.GetBookshelfSettingsUseCase
import com.sorrowblue.comicviewer.domain.usecase.UpdateBookshelfSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
internal class BookshelfDisplayViewModel @Inject constructor(
    getBookshelfSettingsUseCase: GetBookshelfSettingsUseCase,
    private val updateBookshelfSettingsUseCase: UpdateBookshelfSettingsUseCase,
) : ViewModel() {

    val setting =
        getBookshelfSettingsUseCase.execute(EmptyRequest)
            .let { it as Response.Success }.data

    fun updateDisplay(display: Display) {
        viewModelScope.launch {
            updateBookshelfSettingsUseCase.execute(UpdateBookshelfSettingsRequest {
                it.copy(display = display)
            })
        }
    }

    fun updateSort(sort: BookshelfSettings.Sort) {
        viewModelScope.launch {
            updateBookshelfSettingsUseCase.execute(UpdateBookshelfSettingsRequest {
                it.copy(sort = sort)
            })
        }
    }

    fun updateOrder(order: BookshelfSettings.Order) {
        viewModelScope.launch {
            updateBookshelfSettingsUseCase.execute(UpdateBookshelfSettingsRequest {
                it.copy(order = order)
            })
        }
    }
}
