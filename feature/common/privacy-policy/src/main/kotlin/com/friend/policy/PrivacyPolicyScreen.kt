package com.friend.policy

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.friend.designsystem.R as Res
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPadding
import com.friend.ui.common.AppToolbar
import com.friend.ui.components.AppScaffold
import com.friend.ui.components.AppText
import com.friend.ui.preview.LightPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyScreen(
    onBackClick: () -> Unit,
) {
    AppScaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            AppToolbar(
                title = stringResource(Res.string.title_privacy_policy),
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
                .verticalScroll(rememberScrollState())
                .imePadding()
                .appPadding(SpacingToken.medium)
        ) {
            AppText(
                text = stringResource(Res.string.msg_privacy_policy),
                maxLines = Int.MAX_VALUE
            )
        }
    }
}

@Composable
@LightPreview
private fun ScreenPreview() {
    PrivacyPolicyScreen {

    }
}