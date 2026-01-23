package com.friend.reportabuse

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPadding
import com.friend.ui.common.AppToolbar
import com.friend.ui.components.AppElevatedButton
import com.friend.ui.components.AppOutlineTextField
import com.friend.ui.components.AppScaffold
import com.friend.ui.preview.LightPreview
import com.friend.designsystem.R as Res


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportAbuseScreen(
    username: String,
    uiState: UiState,
    uiAction: (UiAction) -> Unit,
    onBackClick: () -> Unit,
) {
    AppScaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            AppToolbar(
                title = stringResource(Res.string.title_report_user),
                onBackClick = {
                    onBackClick.invoke()
                })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .consumeWindowInsets(padding)
                .navigationBarsPadding()
                .imePadding()
                .appPadding(SpacingToken.medium)
        ) {
            AppOutlineTextField(
                error = if(uiState.description.isDirty) stringResource(Res.string.error_empty_not_allowed) else null,
                text = uiState.description.value,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 5,
                title = stringResource(Res.string.label_why_report_user),
                placeholder = stringResource(Res.string.hint_write_here),
                onValueChange = { uiAction.invoke(UiAction.OnTextChange(it)) },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    autoCorrectEnabled = false,
                ),
            )

            Spacer(modifier = Modifier.height(SpacingToken.huge))

            AppElevatedButton(
                isLoading = uiState.isSubmitting,
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.title_report_user),
                onClick = {
                    uiAction.invoke(UiAction.SubmitReport(username))
                },
            )
        }
    }
}

@Composable
@LightPreview
private fun ScreenPreview() {
    ReportAbuseScreen(
        username = "",
        uiState = UiState(),
        uiAction = {},
        onBackClick = {}
    )
}