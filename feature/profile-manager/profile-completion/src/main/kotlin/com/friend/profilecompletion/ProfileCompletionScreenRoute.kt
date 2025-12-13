package com.friend.profilecompletion

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun ProfileCompletionScreenRoute(
    onBackButtonClicked: () -> Unit,
    onContinueButtonClicked: () -> Unit
){
    val context = LocalContext.current
    //val state by viewModel.uiState.collectAsState()

//    LaunchedEffect(Unit) {
//        viewModel.action(UiAction.ResetState)
//        viewModel.uiEffect.collect { effect ->
//            when (effect) {
//                is UiEvent.ShowMessage ->
//                    context.showToastMessage(effect.message)
//
//                is UiEvent.NavigateToHome ->
//                    navigateToHome()
//            }
//        }
//    }

    ProfileCompletionScreen(
        onBackButtonClicked = onBackButtonClicked,
        onContinueButtonClicked = onContinueButtonClicked
    )
}