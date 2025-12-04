package com.friend.overview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.friend.common.constant.PersonalMenu
import com.friend.ui.showToastMessage

@Composable
fun ProfileOverviewRoute(
    onBackButtonClicked: () -> Unit,
    navigateToProfileScreen: () -> Unit,
    navigateToLoginScreen: () -> Unit,
    clickedOnMenu: (PersonalMenu) -> Unit,
    viewModel: ProfileOverviewViewModel = hiltViewModel()
) {
    val userInfo by viewModel.userInfo.collectAsState()
    val state by viewModel.uiState.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                UiEvent.NavigateToLoginScreen -> navigateToLoginScreen.invoke()
                is UiEvent.ShowMessage -> context.showToastMessage(event.message)
            }
        }
    }

    ProfileOverviewScreen(
        userInfo,
        onBackButtonClicked = onBackButtonClicked,
        navigateToProfileScreen = navigateToProfileScreen,
        clickedOnMenu = {
            clickedOnMenu.invoke(it)
        }
    )
}