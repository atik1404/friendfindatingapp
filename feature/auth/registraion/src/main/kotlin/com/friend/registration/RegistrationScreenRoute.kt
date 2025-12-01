package com.friend.registration

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun RegistrationRoute(
    onBackButtonClicked: () -> Unit,
    navigateToProfileCompletion: () -> Unit,
    viewModel: RegistrationViewModel = hiltViewModel()
) {
    RegistrationScreen {
        onBackButtonClicked.invoke()
    }
}