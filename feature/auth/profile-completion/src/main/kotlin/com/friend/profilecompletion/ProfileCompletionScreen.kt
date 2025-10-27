package com.friend.profilecompletion

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.friend.ui.components.AppScaffold
import com.friend.ui.preview.LightDarkPreview

@Composable
fun ProfileCompletionScreen() {
    AppScaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
    ) { padding ->
        Text("Profile completion Screen")
    }
}

@Composable
@LightDarkPreview
private fun ScreenPreview() {
    ProfileCompletionScreen()
}