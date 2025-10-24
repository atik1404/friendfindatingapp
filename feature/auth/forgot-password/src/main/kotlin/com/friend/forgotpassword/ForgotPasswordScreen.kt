package com.friend.forgotpassword

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.friend.ui.components.AppScaffold

@Composable
fun ForgotPasswordScreen() {
    AppScaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
    ) { padding ->
        Text("Forgot Password Screen")
    }
}