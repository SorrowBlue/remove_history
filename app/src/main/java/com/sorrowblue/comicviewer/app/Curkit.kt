package com.sorrowblue.comicviewer.app

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import com.sorrowblue.comicviewer.domain.usecase.paging.PagingBookshelfFolderUseCase
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import kotlinx.parcelize.Parcelize
import logcat.logcat


@Parcelize
data object InboxScreen : Screen {

    data class State(
        val emails: List<String>,
        val eventSink: (Event) -> Unit
    ) : CircuitUiState

    sealed class Event : CircuitUiEvent {
        data class EmailClicked(val emailId: String) : Event()
    }
}

@Composable
private fun EmailItem(email: String, modifier: Modifier = Modifier) {
    ListItem(headlineContent = {
        Text(text = email)
    }, modifier = modifier, )
}


@CircuitInject(InboxScreen::class, SingletonComponent::class)
class InboxUi : Ui<InboxScreen.State> {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content(state: InboxScreen.State, modifier: Modifier) {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            topBar = { TopAppBar(title = { Text("Inbox") }) }) { innerPadding ->
            LazyColumn(modifier = Modifier.padding(innerPadding)) {
                items(state.emails) { email ->
                    EmailItem(email, modifier = Modifier.clickable { state.eventSink(InboxScreen.Event.EmailClicked(email)) })
                }
            }
        }
    }
}

@CircuitInject(InboxScreen::class, ViewModelComponent::class)
class InboxPresenter @Inject constructor(private val navigator: Navigator) : Presenter<InboxScreen.State> {
    @Composable
    override fun present(): InboxScreen.State {
        return InboxScreen.State(
            emails = listOf(
                "all@example.com"
            )
        ){ event ->
            logcat { "event=$event" }
            when (event) {
                // Navigate to the detail screen when an email is clicked
                is InboxScreen.Event.EmailClicked -> navigator.goTo(DetailScreen(event.emailId))
            }
        }
    }
}

@Parcelize
data class DetailScreen(val emailId: String) : Screen {
    data class State(val email: String, val eventSink: (Event) -> Unit) : CircuitUiState {
    }


    sealed class Event : CircuitUiEvent {
        data object BackClicked : Event()
    }
}

@CircuitInject(DetailScreen::class, SingletonComponent::class)
class DetailPresenter(
    private val navigator: Navigator,
    private val screen: DetailScreen,
    private val emailRepository: EmailRepository = EmailRepository
) : Presenter<DetailScreen.State> {
    @Composable
    override fun present(): DetailScreen.State {
        val email = emailRepository.getEmail(screen.emailId)
        return DetailScreen.State(email) { event ->
            when (event) {
                DetailScreen.Event.BackClicked -> navigator.pop()
            }
        }
    }
}

object EmailRepository {
    fun getEmail(emailId: String): String {
        return emailId
    }

}

@CircuitInject(DetailScreen::class, SingletonComponent::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailDetail(state: DetailScreen.State, modifier: Modifier = Modifier) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.email) },
                navigationIcon = {
                    IconButton(onClick = { state.eventSink(DetailScreen.Event.BackClicked) }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ){
        Text(text = "email is " + state.email, modifier = Modifier.padding(it))
    }
}

