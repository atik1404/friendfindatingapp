package com.friend.home

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun HomeRoute(
    navigateToChatListScreen: () -> Unit,
    navigateToOverviewScreen: () -> Unit,
    navigateToProfileScreen: (String, String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    HomeScreen(
        fullName = viewModel.fullName.value,
        navigateToChatListScreen = {},
        navigateToOverviewScreen = {},
        navigateToProfileScreen = { _, _ ->

        },
    )
}