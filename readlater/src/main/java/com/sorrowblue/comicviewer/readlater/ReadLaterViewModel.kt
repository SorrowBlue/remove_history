package com.sorrowblue.comicviewer.readlater

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.sorrowblue.comicviewer.domain.usecase.paging.PagingReadLaterFileUseCase
import com.sorrowblue.comicviewer.domain.usecase.settings.ManageFolderDisplaySettingsUseCase
import com.sorrowblue.comicviewer.file.list.FileListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class ReadLaterViewModel @Inject constructor(
    manageFolderDisplaySettingsUseCase: ManageFolderDisplaySettingsUseCase,
    pagingReadLaterFileUseCase: PagingReadLaterFileUseCase,
) : FileListViewModel(manageFolderDisplaySettingsUseCase) {

    override val transitionName = null

    override val pagingDataFlow = pagingReadLaterFileUseCase
        .execute(PagingReadLaterFileUseCase.Request(PagingConfig(20)))
        .cachedIn(viewModelScope)
}
