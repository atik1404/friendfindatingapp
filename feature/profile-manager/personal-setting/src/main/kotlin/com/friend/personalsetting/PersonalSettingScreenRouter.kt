package com.friend.personalsetting

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.friend.ui.common.asString
import com.friend.ui.showToastMessage

@Composable
fun PersonalSettingScreenRouter(
    onBackButtonClicked: () -> Unit,
    viewmodel: PersonalSettingViewmodel = hiltViewModel()
) {
    val uiState by viewmodel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewmodel.action(UiAction.ResetState)
        viewmodel.action(UiAction.FetchCountry)
        viewmodel.action(UiAction.SetDefaultData)

        viewmodel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                UiEvent.FinishScreen -> onBackButtonClicked.invoke()
                is UiEvent.ShowToastMessage -> context.showToastMessage(
                    uiEvent.message.asString(
                        context
                    )
                )
            }
        }
    }

    PersonalSettingScreen(
        onBackButtonClicked = onBackButtonClicked,
        state = uiState,
        onAction = {
            viewmodel.action(it)
        }
    )
}