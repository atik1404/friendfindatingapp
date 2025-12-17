package com.friend.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SplashRoute(
    navigateToLoginScreen: () -> Unit = {},
    navigateToHomeScreen: () -> Unit = {},
    viewModel: SplashViewModel = hiltViewModel(),
) {
    val state by viewModel.uiSate.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.action(UiEvent.CheckLoginStatus)
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is UiEffect.NavigateToLogin -> navigateToLoginScreen()
                is UiEffect.NavigateToHome -> navigateToHomeScreen()
            }
        }
    }

    SplashScreen(
        uiState = state
    ) {
        viewModel.action(it)
    }
}