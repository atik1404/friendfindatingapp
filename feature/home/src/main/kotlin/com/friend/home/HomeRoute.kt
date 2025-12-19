package com.friend.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun HomeRoute(
    navigateToChatListScreen: () -> Unit,
    navigateToOverviewScreen: () -> Unit,
    navigateToProfileScreen: () -> Unit,
    navigateToOtherProfileScreen: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {

    val fullName by viewModel.fullName.collectAsState()
    val profilePicture by viewModel.profilePicture.collectAsState()
    val uiSate by viewModel.uiState.collectAsState()

    HomeScreen(
        fullName = fullName,
        profilePicture = profilePicture,
        state = uiSate,
        navigateToChatListScreen = navigateToChatListScreen,
        navigateToOverviewScreen = navigateToOverviewScreen,
        onEvent = {
            viewModel.action
        },
        navigateToProfileScreen = navigateToProfileScreen,
        navigateToOtherProfileScreen = { navigateToOtherProfileScreen.invoke(it) },
    )
}