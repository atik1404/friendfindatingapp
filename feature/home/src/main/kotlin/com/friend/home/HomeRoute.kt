package com.friend.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import timber.log.Timber

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
    val filterUiSate by viewModel.filterUiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.action(UiAction.SetCurrentUserInfo)
        viewModel.action(UiAction.FetchFriendSuggestion)
    }

    HomeScreen(
        fullName = fullName,
        profilePicture = profilePicture,
        state = uiSate,
        navigateToChatListScreen = navigateToChatListScreen,
        navigateToOverviewScreen = navigateToOverviewScreen,
        onEvent = {
            viewModel.action(it)
        },
        navigateToProfileScreen = navigateToProfileScreen,
        navigateToOtherProfileScreen = { navigateToOtherProfileScreen.invoke(it) },
        filterUiState = filterUiSate
    )
}