package com.friend.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.friend.designsystem.theme.backgroundColors
import com.friend.ui.components.AppButtonVariant
import com.friend.ui.components.AppFilledButton
import com.friend.ui.components.AppScaffold
import com.friend.ui.components.LoadLocalImage
import com.friend.designsystem.R as Res

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen() {
    AppScaffold(
        contentWindowInsets = WindowInsets(0),
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            LoadLocalImage(
                imageResId = Res.drawable.img_login_illustration,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentScale = ContentScale.FillHeight
            )

            AppFilledButton(
                modifier = Modifier.fillMaxWidth(),
                buttonText = "Login with Friend",
                onClick = { /* TODO: Handle login action */ },
                variant = AppButtonVariant.Primary
            )
        }
    }
}

@Composable
@PreviewLightDark
fun LoginScreenPreview() {
    LoginScreen()
}