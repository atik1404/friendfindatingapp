package com.friend.registration

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.friend.ui.common.asString
import com.friend.ui.showToastMessage

@Composable
fun RegistrationRoute(
    onBackButtonClicked: () -> Unit,
    navigateToProfileCompletion: () -> Unit,
    viewModel: RegistrationViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.action(UiAction.ResetState)
        viewModel.action(UiAction.FetchCountry)

        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowToastMessage -> context.showToastMessage(
                    event.message.asString(
                        context
                    )
                )
            }
        }
    }

    RegistrationScreen(
        onBackButtonClicked = onBackButtonClicked,
        state = state,
        uiAction = {
            viewModel.action(it)
        }
    )
}