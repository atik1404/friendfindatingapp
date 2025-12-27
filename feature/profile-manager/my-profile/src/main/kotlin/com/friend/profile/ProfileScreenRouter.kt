package com.friend.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.friend.ui.common.asString
import com.friend.ui.showToastMessage

@Composable
fun ProfileScreenRouter(
    onBackButtonClicked: () -> Unit,
    navigateToEditProfile: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val imageUiState by viewModel.imageUiSate.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.action(UiAction.FetchProfile)
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

    ProfileScreen(
        uiState = uiState,
        onBackButtonClicked = onBackButtonClicked,
        navigateToEditProfile = navigateToEditProfile,
        imageUiState = imageUiState,
        onAction = {
            viewModel.action(it)
        }
    )
}