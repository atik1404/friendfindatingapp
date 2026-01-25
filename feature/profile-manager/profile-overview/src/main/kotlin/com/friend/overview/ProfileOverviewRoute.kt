package com.friend.overview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.friend.common.constant.PersonalMenu
import com.friend.common.extfun.openAppInPlayStore
import com.friend.common.extfun.openMailApp
import com.friend.common.extfun.shareApp
import com.friend.common.utils.GoogleSignInManager
import com.friend.ui.showToastMessage
import kotlinx.coroutines.launch

@Composable
fun ProfileOverviewRoute(
    onBackButtonClicked: () -> Unit,
    navigateToProfileScreen: () -> Unit,
    navigateToLoginScreen: () -> Unit,
    navigateToChangePasswordScreen: () -> Unit,
    navigateToPrivacyPolicyScreen: () -> Unit,
    navigateToPersonalSettingScreen: () -> Unit,
    navigateToMembershipScreen: () -> Unit,
    exitApp: () -> Unit,
    viewModel: ProfileOverviewViewModel = hiltViewModel()
) {
    val userInfo by viewModel.userInfo.collectAsStateWithLifecycle()
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    var isWarningDialogVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val scope = rememberCoroutineScope()
    val googleSignInManager = remember { GoogleSignInManager(context) }

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
                PersonalMenu.LOGOUT -> {
                    viewModel.action(UiAction.PerformLogout)
                    if (userInfo.isLoginByGoogle) {
                        scope.launch {
                            googleSignInManager.signOut()
                        }
                    }
                }

                PersonalMenu.SHARE_APP -> context.shareApp()
                PersonalMenu.RATE_APP -> context.openAppInPlayStore()
                PersonalMenu.CONTACT_US -> context.openMailApp()
                PersonalMenu.EXIT_APP -> exitApp.invoke()
                PersonalMenu.DELETE_ACCOUNT -> {
                    isWarningDialogVisible = true
                }
            }
        }
    )

    if (isWarningDialogVisible)
        DeleteAccountBottomSheet(
            onDismissRequest = {
                isWarningDialogVisible = false
            }
        ) {
            isWarningDialogVisible = false
            viewModel.action(UiAction.PerformDeleteAccount)
        }
}