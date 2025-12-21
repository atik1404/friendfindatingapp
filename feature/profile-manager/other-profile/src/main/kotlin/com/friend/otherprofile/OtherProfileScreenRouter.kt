package com.friend.otherprofile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.friend.ui.common.asString
import com.friend.ui.showToastMessage

@Composable
fun OtherProfileScreenRouter(
    username: String,
    onBackButtonClicked: () -> Unit,
    onNavigateToMessageRoom: () -> Unit,
    navigateToReportAbuse: () -> Unit,
    viewModel: OtherProfileViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isBlocked by viewModel.isBlocked

    LaunchedEffect(Unit) {
        viewModel.action(UiAction.FetchProfile(username))
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowToastMessage -> context.showToastMessage(
                    event.uiText.asString(
                        context
                    )
                )
            }
        }
    }

    OtherProfileScreen(
        uiState = uiState,
        isBlocked = isBlocked,
        username = username,
        onBackButtonClicked = onBackButtonClicked,
        navigateToMessageRoom = onNavigateToMessageRoom,
        navigateToReportAbuse = navigateToReportAbuse,
        uiAction = {
            viewModel.action(it)
        }
    )
}