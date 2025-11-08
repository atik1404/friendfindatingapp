package com.friend.profile

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.friend.designsystem.R as Res
import com.friend.ui.common.AppToolbar
import com.friend.ui.components.AppScaffold
import com.friend.ui.preview.LightDarkPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    username: String,
    userId: String,
    onBackButtonClicked: () -> Unit
){
    AppScaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
    ) { padding ->

    }
}

@Composable
@LightDarkPreview
private fun ScreenPreview() {
    ProfileScreen(
        username = "",
        userId = "",
        onBackButtonClicked = {},
    )
}