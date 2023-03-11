package com.sorrowblue.comicviewer.folder.display

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sorrowblue.comicviewer.domain.entity.settings.FolderDisplaySettings
import com.sorrowblue.comicviewer.domain.usecase.settings.ManageFolderDisplaySettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@HiltViewModel
internal class FolderDisplayViewModel @Inject constructor(
    private val manageFolderDisplaySettingsUseCase: ManageFolderDisplaySettingsUseCase,
) : ViewModel() {

    fun update(display: FolderDisplaySettings.Display) {
        viewModelScope.launch {
            manageFolderDisplaySettingsUseCase.edit { it.copy(display = display) }
        }
    }

    fun update(size: FolderDisplaySettings.Size) {
        viewModelScope.launch {
            manageFolderDisplaySettingsUseCase.edit { it.copy(columnSize = size) }
        }
    }

    fun update(sort: FolderDisplaySettings.Sort) {
        viewModelScope.launch {
            manageFolderDisplaySettingsUseCase.edit { it.copy(sort = sort) }
        }
    }

    fun update(order: FolderDisplaySettings.Order) {
        viewModelScope.launch {
            manageFolderDisplaySettingsUseCase.edit { it.copy(order = order) }
        }
    }

    val displayFlow =
        manageFolderDisplaySettingsUseCase.settings.map { it.display }.distinctUntilChanged()
    val columnSizeFlow =
        manageFolderDisplaySettingsUseCase.settings.map { it.columnSize }.distinctUntilChanged()
    val sortFlow =
        manageFolderDisplaySettingsUseCase.settings.map { it.sort }.distinctUntilChanged()
    val orderFlow =
        manageFolderDisplaySettingsUseCase.settings.map { it.order }.distinctUntilChanged()
}
