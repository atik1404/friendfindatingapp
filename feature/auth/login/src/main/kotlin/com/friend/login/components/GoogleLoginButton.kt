package com.friend.login.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPaddingHorizontal
import com.friend.login.signInWithGoogle
import com.friend.ui.components.AppOutlinedButton
import kotlinx.coroutines.launch
import com.friend.designsystem.R as Res

@Composable
fun GoogleLoginButton(
    modifier: Modifier = Modifier,
    onIdToken: (String) -> Unit = {},
    onError: (Throwable) -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var isLoading by rememberSaveable { mutableStateOf(false) }

    AppOutlinedButton(
        modifier = modifier
            .fillMaxWidth()
            .appPaddingHorizontal(SpacingToken.large),
        text = stringResource(Res.string.action_login_with_google),
        onClick = {
            scope.launch {
                runCatching { signInWithGoogle(context) }
                    .onSuccess(onIdToken)
                    .onFailure(onError)
            }
        },
        isLoading = isLoading
    )
}