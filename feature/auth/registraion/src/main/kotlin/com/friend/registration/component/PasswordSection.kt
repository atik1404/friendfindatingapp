package com.friend.registration.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.friend.designsystem.R as Res
import com.friend.ui.components.AppOutlineTextField

@Composable
fun PasswordSection(
    modifier: Modifier = Modifier
) {
    AppOutlineTextField(
        text = "",
        modifier = modifier.fillMaxWidth(),
        title = stringResource(Res.string.label_password),
        placeholder = stringResource(Res.string.hint_password),
        onValueChange = { },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
        ),
        isPassword = true
    )
}