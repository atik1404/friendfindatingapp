package com.friend.reportabuse

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.friend.ui.common.asString
import com.friend.ui.showToastMessage

@Composable
fun ReportAbuseScreenRoute(
    username: String,
    onBackClick: () -> Unit,
    viewModel: ReportAbuseViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                UiEvent.FinishScreen -> onBackClick.invoke()
                is UiEvent.ShowToastMessage -> context.showToastMessage(
                    event.uiText.asString(
                        context
                    )
                )
            }
        }
    }

    ReportAbuseScreen(
        username = username,
        uiState = uiState,
        uiAction = viewModel.action,
        onBackClick = onBackClick
    )
}