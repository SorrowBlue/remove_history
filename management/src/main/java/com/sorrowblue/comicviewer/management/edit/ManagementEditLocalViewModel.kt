package com.sorrowblue.comicviewer.management.edit

import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sorrowblue.comicviewer.domain.model.library.Library
import com.sorrowblue.comicviewer.domain.model.library.RegisterLibraryRequest
import com.sorrowblue.comicviewer.domain.model.library.SupportProtocol
import com.sorrowblue.comicviewer.domain.usecase.RegisterLibraryUseCase
import com.sorrowblue.comicviewer.framework.ui.navigation.SupportSafeArgs
import com.sorrowblue.comicviewer.framework.ui.navigation.navArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import logcat.logcat

@HiltViewModel
internal class ManagementEditLocalViewModel @Inject constructor(
    private val registerLibraryUseCase: RegisterLibraryUseCase,
    override val savedStateHandle: SavedStateHandle,
) : ViewModel(), SupportSafeArgs {

    private val args: ManagementEditSmbFragmentArgs by navArgs()

    val displayName = MutableStateFlow(args.library?.name.orEmpty())
    val dir = MutableStateFlow("")


    val isConnecting = MutableStateFlow(false)
    val result = registerLibraryUseCase.source

    fun connect(done: DocumentFile, function: () -> Unit) {
        isConnecting.value = true
        val library = Library(
            displayName.value,
            done.uri.host.orEmpty(),
            done.uri.encodedPath.orEmpty(),
            "",
            SupportProtocol.Local,
            "",
            ""
        ).also {
            logcat { done.uri.toString() }
            logcat { it.toString() }
        }
        viewModelScope.launch {
            registerLibraryUseCase.execute(RegisterLibraryRequest(library))
            isConnecting.value = false
        }
    }

    val message = MutableSharedFlow<String>(0, 1, BufferOverflow.DROP_OLDEST)
}
