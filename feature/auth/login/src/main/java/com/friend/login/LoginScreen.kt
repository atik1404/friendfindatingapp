package com.friend.login

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.friend.designsystem.spacing.RadiusToken
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.StrokeTokens
import com.friend.designsystem.theme.backgroundColors
import com.friend.designsystem.theme.strokeColors
import com.friend.designsystem.theme.textColors
import com.friend.designsystem.typography.AppTypography
import com.friend.ui.components.AppElevatedButton
import com.friend.ui.components.AppOutlineTextField
import com.friend.ui.components.AppScaffold
import com.friend.ui.components.AppText
import com.friend.ui.components.LoadLocalImage
import com.friend.designsystem.R as Res

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen() {
    AppScaffold(
        contentWindowInsets = WindowInsets(0),
    ) { padding ->
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            val guideline = createGuidelineFromTop(.35f)
            val (bannerImage, loginUi) = createRefs()

            var email by rememberSaveable { mutableStateOf("") }
            var password by rememberSaveable { mutableStateOf("") }

            LoadLocalImage(
                imageResId = Res.drawable.img_login_illustration,
                modifier = Modifier.constrainAs(bannerImage) {
                    top.linkTo(parent.top, SpacingToken.large)
                    bottom.linkTo(guideline)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                },
                contentScale = ContentScale.Fit
            )

            Column(
                modifier = Modifier
                    .padding(SpacingToken.medium)
                    .border(
                        width = StrokeTokens.thin,
                        color = MaterialTheme.strokeColors.primary,
                        shape = RoundedCornerShape(RadiusToken.large)
                    )
                    .constrainAs(loginUi) {
                        top.linkTo(guideline)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        height = Dimension.preferredWrapContent
                    }
                    .padding(SpacingToken.medium),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AppOutlineTextField(
                    text = email,
                    modifier = Modifier.fillMaxWidth(),
                    title = "Email address*",
                    placeholder = "Enter your Email",
                    onValueChange = {
                        email = it
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )

                Spacer(Modifier.height(SpacingToken.medium))

                AppOutlineTextField(
                    text = password,
                    modifier = Modifier.fillMaxWidth(),
                    title = "Password*",
                    placeholder = "Enter your Password",
                    onValueChange = {
                        password = it
                    },
                    isPassword = true
                )

                Spacer(modifier = Modifier.height(SpacingToken.medium))

                AppElevatedButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Login",
                    onClick = {

                    },
                )
                Spacer(modifier = Modifier.height(SpacingToken.medium))
                AppText(
                    text = "Forgot Password?",
                    fontWeight = FontWeight.Medium,
                    textColor = MaterialTheme.textColors.primary,
                    modifier = Modifier.wrapContentWidth(Alignment.End),
                )

                Spacer(modifier = Modifier.height(SpacingToken.medium))

                AppText(
                    text = "Don't have an account? Sign Up",
                    fontWeight = FontWeight.Medium,
                    textColor = MaterialTheme.textColors.secondary,
                    modifier = Modifier
                )
            }
        }
    }
}

@Composable
@PreviewLightDark
fun LoginScreenPreview() {
    LoginScreen()
}