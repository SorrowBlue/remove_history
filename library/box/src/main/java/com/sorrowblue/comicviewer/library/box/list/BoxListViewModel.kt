package com.sorrowblue.comicviewer.library.box.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.sorrowblue.comicviewer.framework.ui.navigation.SupportSafeArgs
import com.sorrowblue.comicviewer.framework.ui.navigation.navArgs
import com.sorrowblue.comicviewer.framework.ui.navigation.stateIn
import com.sorrowblue.comicviewer.library.box.data.BoxApiRepository
import com.sorrowblue.comicviewer.library.filelist.LibraryFileListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn

@OptIn(ExperimentalCoroutinesApi::class)
internal class BoxListViewModel(
    private val repository: BoxApiRepository,
    override val savedStateHandle: SavedStateHandle
) : LibraryFileListViewModel(), SupportSafeArgs {

    private val args: BoxListFragmentArgs by navArgs()
    override val transitionName = args.transitionName
    override val isAuthenticated: Flow<Boolean> =
        repository.isAuthenticated().flowOn(Dispatchers.IO)

    override val pagingDataFlow = repository.userInfoFlow.filterNotNull().flatMapLatest {
        Pager(PagingConfig(20)) { BoxPagingSource(args.parent, repository) }.flow
            .cachedIn(viewModelScope)
    }

    val userInfoFlow = repository.userInfoFlow.stateIn { null }

    suspend fun accessToken() = repository.accessToken()

    companion object {

        fun Factory(repository: BoxApiRepository) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                // Create a SavedStateHandle for this ViewModel from extras
                val savedStateHandle = extras.createSavedStateHandle()
                return BoxListViewModel(repository, savedStateHandle) as T
            }
        }
    }
}
