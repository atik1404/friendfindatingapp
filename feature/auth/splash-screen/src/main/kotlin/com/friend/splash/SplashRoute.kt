package com.friend.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun SplashRoute(
    navigateToLoginScreen: () -> Unit = {},
    navigateToHomeScreen: () -> Unit = {},
    viewModel: SplashViewModel = hiltViewModel(),
) {
    viewModel.action(UiEvent.CheckLoginStatus)

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is UiEffect.NavigateToLogin -> navigateToLoginScreen()
                is UiEffect.NavigateToHome -> navigateToHomeScreen()
            }
        }
    }

    SplashScreen()
}