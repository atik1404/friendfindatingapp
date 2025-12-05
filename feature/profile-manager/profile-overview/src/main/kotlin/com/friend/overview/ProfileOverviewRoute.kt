package com.friend.overview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.friend.common.constant.PersonalMenu
import com.friend.common.extfun.openAppInPlayStore
import com.friend.common.extfun.openMailApp
import com.friend.common.extfun.shareApp
import com.friend.ui.showToastMessage

@Composable
fun ProfileOverviewRoute(
    onBackButtonClicked: () -> Unit,
    navigateToProfileScreen: () -> Unit,
    navigateToLoginScreen: () -> Unit,
    navigateToChangePasswordScreen: () -> Unit,
    navigateToPrivacyPolicyScreen: () -> Unit,
    navigateToPersonalSettingScreen: () -> Unit,
    navigateToMembershipScreen: () -> Unit,
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
        state = state,
        onBackButtonClicked = onBackButtonClicked,
        navigateToProfileScreen = navigateToProfileScreen,
        clickedOnMenu = {
            when (it) {
                PersonalMenu.PERSONAL_SETTING -> navigateToPersonalSettingScreen.invoke()
                PersonalMenu.PRIVACY_POLICY -> navigateToPrivacyPolicyScreen.invoke()
                PersonalMenu.CHANGE_PASSWORD -> navigateToChangePasswordScreen.invoke()
                PersonalMenu.VIP_MEMBERSHIP -> navigateToMembershipScreen.invoke()
                PersonalMenu.LOGOUT -> viewModel.action(UiAction.PerformLogout)
                PersonalMenu.SHARE_APP -> context.shareApp()
                PersonalMenu.RATE_APP -> context.openAppInPlayStore()
                PersonalMenu.CONTACT_US -> context.openMailApp()
            }
        }
    )
}