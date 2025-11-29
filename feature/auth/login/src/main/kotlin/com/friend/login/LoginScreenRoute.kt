package com.friend.login

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.friend.ui.showToastMessage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginRoute(
    navigateToRegistration: () -> Unit,
    navigateToForgotPassword: () -> Unit,
    navigateToHome: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is LoginUiEffect.ShowMessage ->
                    context.showToastMessage(effect.message)

                is LoginUiEffect.NavigateToHome ->
                    navigateToHome()
            }
        }
    }

    LoginScreen(
        state = state,
        onEvent = viewModel.action,
        navigateToRegistration = navigateToRegistration,
        navigateToForgotPassword = navigateToForgotPassword,
        onGoogleLoginClick = {
            // you can move this into ViewModel later if needed
            context.showToastMessage("This is toast message")
        },
    )
}