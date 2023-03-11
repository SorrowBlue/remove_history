package com.sorrowblue.comicviewer.file.list

import com.sorrowblue.comicviewer.domain.entity.file.File
import com.sorrowblue.comicviewer.domain.usecase.settings.ManageFolderDisplaySettingsUseCase
import com.sorrowblue.comicviewer.framework.ui.fragment.PagingViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

abstract class FileListViewModel(manageFolderDisplaySettingsUseCase: ManageFolderDisplaySettingsUseCase) :
    PagingViewModel<File>() {
    val isEnabledThumbnailFlow =
        manageFolderDisplaySettingsUseCase.settings.map { it.isEnabledThumbnail }.distinctUntilChanged()
    val displayFlow =
        manageFolderDisplaySettingsUseCase.settings.map { it.display }.distinctUntilChanged()
    val sortFlow =
        manageFolderDisplaySettingsUseCase.settings.map { it.sort }.distinctUntilChanged()
    val orderFlow =
        manageFolderDisplaySettingsUseCase.settings.map { it.order }.distinctUntilChanged()
    val columnSizeFlow =
        manageFolderDisplaySettingsUseCase.settings.map { it.columnSize }.distinctUntilChanged()
}
