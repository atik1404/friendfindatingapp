package com.friend.login.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.friend.common.utils.GoogleSignInManager
import com.friend.common.utils.GoogleSignInResult
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPaddingHorizontal
import com.friend.ui.components.AppOutlinedButton
import kotlinx.coroutines.launch
import com.friend.designsystem.R as Res

@Composable
fun GoogleLoginButton(
    modifier: Modifier = Modifier,
    onIdToken: (String, String, String) -> Unit,
    onError: (String) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var isLoading by rememberSaveable { mutableStateOf(false) }
    val googleSignInManager = remember { GoogleSignInManager(context) }

    AppOutlinedButton(
        modifier = modifier
            .fillMaxWidth()
            .appPaddingHorizontal(SpacingToken.large),
        text = stringResource(Res.string.action_login_with_google),
        onClick = {
            scope.launch {
                when (val result = googleSignInManager.signIn()) {
                    is GoogleSignInResult.Success -> {
                        onIdToken.invoke(result.displayName ?: "", result.email, result.idToken)
                    }

                    is GoogleSignInResult.Error -> {
                        onError.invoke(result.message)
                    }
                }
            }
        },
        isLoading = isLoading
    )
}