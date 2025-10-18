package com.friend.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.friend.designsystem.spacing.SpacingToken
import com.friend.ui.components.AppElevatedButton
import com.friend.ui.components.AppScaffold
import com.friend.ui.components.AppTextField
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
                .padding(SpacingToken.medium),
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

            AppTextField(
                value = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                label = "Email",
                placeholder = "Enter your email",
                onValueChange = {},
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
            )

            AppTextField(
                isPassword = true,
                value = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                label = "Password",
                placeholder = "Enter your Password",
                onValueChange = {},
            )

            Spacer(modifier = Modifier.height(SpacingToken.large))

            AppElevatedButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Login",
                onClick = {

                },
            )
        }
    }
}

@Composable
@PreviewLightDark
fun LoginScreenPreview() {
    LoginScreen()
}