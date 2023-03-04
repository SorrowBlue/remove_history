package com.sorrowblue.comicviewer.readlater

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.sorrowblue.comicviewer.domain.entity.file.File
import com.sorrowblue.comicviewer.domain.usecase.paging.PagingReadLaterFileUseCase
import com.sorrowblue.comicviewer.domain.usecase.settings.ManageFolderDisplaySettingsUseCase
import com.sorrowblue.comicviewer.framework.ui.fragment.PagingViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@HiltViewModel
internal class ReadLaterViewModel @Inject constructor(
    manageFolderDisplaySettingsUseCase: ManageFolderDisplaySettingsUseCase,
    pagingReadLaterFileUseCase: PagingReadLaterFileUseCase,
) : PagingViewModel<File>() {

    override val transitionName = null
    override val pagingDataFlow = pagingReadLaterFileUseCase
        .execute(PagingReadLaterFileUseCase.Request(PagingConfig(20)))
        .cachedIn(viewModelScope)

    val displayFlow =
        manageFolderDisplaySettingsUseCase.settings.map { it.display }.distinctUntilChanged()
    val spanCountFlow =
        manageFolderDisplaySettingsUseCase.settings.map { it.rawSpanCount }.distinctUntilChanged()
}
