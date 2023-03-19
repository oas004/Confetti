package dev.johnoreilly.confetti.sessions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.johnoreilly.confetti.SessionsUiState
import dev.johnoreilly.confetti.SessionsViewModel
import dev.johnoreilly.confetti.sessiondetails.navigation.SessionDetailsKey
import dev.johnoreilly.confetti.ui.ConfettiAppState
import dev.johnoreilly.confetti.ui.ConfettiScaffold
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel


@Composable
fun SessionsView(
    conference: String,
    appState: ConfettiAppState,
    navigateToSession: (SessionDetailsKey) -> Unit,
    navigateToSignIn: () -> Unit,
    onSignOut: () -> Unit,
    onSwitchConferenceSelected: () -> Unit,
) {
    val viewModel: SessionsViewModel = getViewModel<SessionsViewModel>().apply {
        configure(conference)
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var refreshing by remember { mutableStateOf(false) }
    val refreshScope = rememberCoroutineScope()
    fun refresh() {
        refreshScope.launch {
            refreshing = true
            viewModel.refresh()
            refreshing = false
        }
    }

    ConfettiScaffold(
        conference = conference,
        title = (uiState as? SessionsUiState.Success)?.conferenceName,
        appState = appState,
        onSwitchConference = onSwitchConferenceSelected,
        onSignIn = navigateToSignIn,
        onSignOut = onSignOut,
    ){
        if (appState.isExpandedScreen) {
            SessionListGridView(
                uiState = uiState,
                sessionSelected = navigateToSession,
                onRefresh = ::refresh,
            )
        } else {
            SessionListView(
                uiState = uiState,
                refreshing = refreshing,
                sessionSelected = navigateToSession,
                addBookmark = { viewModel.addBookmark(it) },
                removeBookmark = { viewModel.removeBookmark(it) },
                onRefresh = ::refresh
            )
        }
    }
}