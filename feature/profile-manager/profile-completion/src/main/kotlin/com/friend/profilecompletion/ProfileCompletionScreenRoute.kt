package com.friend.profilecompletion

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.friend.ui.common.asString
import com.friend.ui.showToastMessage

@Composable
fun ProfileCompletionScreenRoute(
    onBackButtonClicked: () -> Unit,
    navigateToHomeScreen: () -> Unit,
    viewModel: ProfileCompletionViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.action(UiAction.ResetState)
        viewModel.uiEvent.collect { event ->
            when (event) {
                UiEvent.NavigateToHome -> navigateToHomeScreen.invoke()
                is UiEvent.ShowToastMessage -> context.showToastMessage(
                    event.message.asString(
                        context
                    )
                )
            }
        }
    }

    ProfileCompletionScreen(
        onBackButtonClicked = onBackButtonClicked,
        state = state,
        onEvent = {
            viewModel.action(it)
        }
    )
}