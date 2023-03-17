package com.sorrowblue.comicviewer.history

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.sorrowblue.comicviewer.domain.usecase.paging.PagingHistoryBookUseCase
import com.sorrowblue.comicviewer.domain.usecase.settings.ManageFolderDisplaySettingsUseCase
import com.sorrowblue.comicviewer.file.list.FileListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class HistoryViewModel @Inject constructor(
    manageFolderDisplaySettingsUseCase: ManageFolderDisplaySettingsUseCase,
    pagingHistoryBookUseCase: PagingHistoryBookUseCase
) : FileListViewModel(manageFolderDisplaySettingsUseCase) {

    override val transitionName = null

    override val pagingDataFlow = pagingHistoryBookUseCase
        .execute(PagingHistoryBookUseCase.Request(PagingConfig(20)))
        .cachedIn(viewModelScope)
}
