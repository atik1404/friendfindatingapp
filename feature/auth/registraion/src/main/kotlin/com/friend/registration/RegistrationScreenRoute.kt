package com.friend.registration

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun RegistrationRoute(
    onBackButtonClicked: () -> Unit,
    navigateToProfileCompletion: () -> Unit,
    viewModel: RegistrationViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    RegistrationScreen(
        onBackButtonClicked = {},
        state = state,
        uiAction = {
            viewModel.action(it)
        }
    )
}