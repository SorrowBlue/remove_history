package com.sorrowblue.comicviewer.settings.viewer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sorrowblue.comicviewer.domain.entity.settings.ViewerSettings
import com.sorrowblue.comicviewer.domain.usecase.settings.ManageViewerSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@HiltViewModel
internal class SettingsViewerViewModel @Inject constructor(
    private val settingsUseCase: ManageViewerSettingsUseCase,
) : ViewModel() {

    val settings = settingsUseCase.settings
    val showStatusBar = settingsUseCase.settings.map { it.showStatusBar }.distinctUntilChanged()
    val showNavigationBar =
        settingsUseCase.settings.map { it.showNavigationBar }.distinctUntilChanged()
    val notTurnOffScreen = settingsUseCase.settings.map { it.keepOnScreen }.distinctUntilChanged()
    val brightnessControl =
        settingsUseCase.settings.map { it.enableBrightnessControl }.distinctUntilChanged()
    val brightnessLevel =
        settingsUseCase.settings.map { it.screenBrightness }.distinctUntilChanged()
    val imageQuality = settingsUseCase.settings.map { it.imageQuality }.distinctUntilChanged()
    val preloadPages = settingsUseCase.settings.map { it.readAheadPageCount }.distinctUntilChanged()

    fun updateStatusBar(newValue: Boolean) {
        viewModelScope.launch {
            settingsUseCase.edit { it.copy(showStatusBar = newValue) }
        }
    }

    fun updateNavigationBar(newValue: Boolean) {
        viewModelScope.launch {
            settingsUseCase.edit { it.copy(showNavigationBar = newValue) }
        }
    }

    fun updateBrightnessLevel(brightness: Float) {
        viewModelScope.launch {
            settingsUseCase.edit { it.copy(screenBrightness = brightness) }
        }
    }

    fun updateReadAheadPageCount(readAheadPageCount: Int) {
        viewModelScope.launch {
            settingsUseCase.edit { it.copy(readAheadPageCount = readAheadPageCount) }
        }
    }

    fun updateBrightnessControl(newValue: Boolean) {
        viewModelScope.launch {
            settingsUseCase.edit { it.copy(enableBrightnessControl = newValue) }
        }
    }

    fun updateKeepOnScreen(newValue: Boolean) {
        viewModelScope.launch {
            settingsUseCase.edit { it.copy(keepOnScreen = newValue) }
        }
    }

    fun updateImageQuality(newValue: Int) {
        viewModelScope.launch {
            settingsUseCase.edit { it.copy(imageQuality = newValue) }
        }
    }

    fun updateBindingDirection(newValue: ViewerSettings.BindingDirection) {
        viewModelScope.launch {
            settingsUseCase.edit { it.copy(bindingDirection = newValue) }
        }
    }
}
