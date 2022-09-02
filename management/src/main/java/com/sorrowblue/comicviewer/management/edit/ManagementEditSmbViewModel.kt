package com.sorrowblue.comicviewer.management.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sorrowblue.comicviewer.domain.model.library.Library
import com.sorrowblue.comicviewer.framework.ui.navigation.SupportSafeArgs
import com.sorrowblue.comicviewer.framework.ui.navigation.navArgs
import com.sorrowblue.comicviewer.domain.model.library.SupportProtocol
import com.sorrowblue.comicviewer.domain.model.library.RegisterLibraryRequest
import com.sorrowblue.comicviewer.domain.usecase.RegisterLibraryUseCase
import com.sorrowblue.comicviewer.management.util.HostNameTextValidator
import com.sorrowblue.comicviewer.management.util.RequireValidator
import com.sorrowblue.comicviewer.management.util.isErrorFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class ClientStatus {
    NONE,
    CONNECTING,
    ERROR,
    DONE
}

enum class AuthType {
    GUEST, USERNAME_PASSWORD
}

@HiltViewModel
internal class ManagementEditSmbViewModel @Inject constructor(
    private val registerLibraryUseCase: RegisterLibraryUseCase,
    override val savedStateHandle: SavedStateHandle,
) : ViewModel(), SupportSafeArgs {

    private val args: ManagementEditSmbFragmentArgs by navArgs()

    val host = MutableStateFlow(args.library?.host.orEmpty())
    val path = MutableStateFlow(args.library?.path.orEmpty().removeSuffix("/"))
    val displayName = MutableStateFlow(args.library?.name.orEmpty())
    val isGuest = MutableStateFlow(false)
    val username = MutableStateFlow("")
    val password = MutableStateFlow("")


    val hostValidations = listOf(RequireValidator(), HostNameTextValidator())
    val usernameValidator = listOf(RequireValidator())
    val passwordValidator = listOf(RequireValidator())
    val isError = combine(
        isGuest,
        hostValidations.isErrorFlow(),
        usernameValidator.isErrorFlow(),
        passwordValidator.isErrorFlow()
    ) { isGuest, host, username, password ->
        if (isGuest)  host else host || username || password
    }.stateIn(viewModelScope, SharingStarted.Lazily, false)

    val isConnecting = MutableStateFlow(false)
    val result = registerLibraryUseCase.source

    fun connect(done: () -> Unit) {
        isConnecting.value = true
        if (isError.value) {
            isConnecting.value = false
            message.tryEmit("入力してください")
            return
        }
        val library = Library(
            displayName.value,
            host.value,
            "/" + path.value + "/",
            "433",
            SupportProtocol.SMB,
            username.value,
            password.value
        )
        viewModelScope.launch {
            registerLibraryUseCase.execute(RegisterLibraryRequest(library))
            isConnecting.value = false
        }
    }

    val message = MutableSharedFlow<String>(0, 1, BufferOverflow.DROP_OLDEST)
}
