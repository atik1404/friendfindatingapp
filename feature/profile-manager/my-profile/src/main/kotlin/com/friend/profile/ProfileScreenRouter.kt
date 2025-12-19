package com.friend.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ProfileScreenRouter(
    onBackButtonClicked: () -> Unit,
    navigateToEditProfile: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.action(UiAction.FetchProfile)
    }

    ProfileScreen(
        uiState = uiState,
        onBackButtonClicked = onBackButtonClicked,
        navigateToEditProfile = navigateToEditProfile,
        onAction = {
            viewModel.action(it)
        }
    )
}