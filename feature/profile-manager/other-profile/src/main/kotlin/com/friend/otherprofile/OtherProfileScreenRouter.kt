package com.friend.otherprofile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun OtherProfileScreenRouter(
    username: String,
    onBackButtonClicked: () -> Unit,
    onNavigateToMessageRoom: () -> Unit,
    navigateToReportAbuse: () -> Unit,
    viewModel: OtherProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.action(UiAction.FetchProfile(username))
    }

    OtherProfileScreen(
        uiState = uiState,
        onBackButtonClicked = onBackButtonClicked,
        navigateToMessageRoom = onNavigateToMessageRoom,
        navigateToReportAbuse = navigateToReportAbuse,
        onRetry = { viewModel.action(UiAction.FetchProfile(username)) }
    )
}