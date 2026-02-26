package com.friend.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun HomeRoute(
    navigateToChatListScreen: () -> Unit,
    navigateToOverviewScreen: () -> Unit,
    navigateToProfileScreen: () -> Unit,
    navigateToMembershipScreen: () -> Unit,
    navigateToOtherProfileScreen: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {

    val fullName by viewModel.fullName.collectAsState()
    val profilePicture by viewModel.profilePicture.collectAsState()
    val uiSate by viewModel.uiState.collectAsState()
    val filterUiSate by viewModel.filterUiState.collectAsStateWithLifecycle()
    val locationUiSate by viewModel.location.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.action(UiAction.CurrentUserInfo)
        viewModel.updateOnlineStatus()
    }

    HomeScreen(
        fullName = fullName,
        profilePicture = profilePicture,
        uiState = uiSate,
        filterUiState = filterUiSate,
        locationState = locationUiSate,
        navigateToChatListScreen = navigateToChatListScreen,
        navigateToOverviewScreen = navigateToOverviewScreen,
        onEvent = {
            viewModel.action(it)
        },
        navigateToProfileScreen = navigateToProfileScreen,
        navigateToOtherProfileScreen = {
            if (it == viewModel.getUsername())//if current user
                navigateToProfileScreen.invoke()//navigate to profile screen
            else navigateToOtherProfileScreen.invoke(it)//navigate to other profile screen
        },
        navigateToMembershipScreen = navigateToMembershipScreen
    )
}