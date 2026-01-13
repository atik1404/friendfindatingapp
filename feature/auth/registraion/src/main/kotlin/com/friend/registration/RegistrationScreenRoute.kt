package com.friend.registration

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.friend.ui.common.asString
import com.friend.ui.showToastMessage

@Composable
fun RegistrationRoute(
    email: String,
    onBackButtonClicked: () -> Unit,
    navigateToProfileCompletion: () -> Unit,
    privacyPolicyClicked: () -> Unit,
    viewModel: RegistrationViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.action(UiAction.OnChangeEmail(email))

        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowToastMessage -> context.showToastMessage(
                    event.message.asString(
                        context
                    )
                )

                UiEvent.NavigateToProfileCompletion -> navigateToProfileCompletion.invoke()
            }
        }
    }

    RegistrationScreen(
        onBackButtonClicked = onBackButtonClicked,
        state = state,
        uiAction = {
            viewModel.action(it)
        },
        privacyPolicyClicked = privacyPolicyClicked
    )
}