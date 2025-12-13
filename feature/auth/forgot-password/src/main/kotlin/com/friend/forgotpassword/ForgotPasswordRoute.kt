package com.friend.forgotpassword

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
fun ForgotPasswordRoute(
    onNavigateBack: () -> Unit,
    viewModel: ForgotPasswordViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.action(UiAction.ResetState)
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is UiEvent.ShowMessage ->
                    context.showToastMessage(effect.message)

                is UiEvent.BackToPreviousScreen -> onNavigateBack.invoke()
            }
        }
    }

    ForgotPasswordScreen(
        onNavigateBack = onNavigateBack,
        state = state,
        onEvent = viewModel.action
    )
}