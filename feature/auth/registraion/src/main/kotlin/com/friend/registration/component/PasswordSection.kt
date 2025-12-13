package com.friend.registration.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.friend.domain.base.TextInput
import com.friend.designsystem.R as Res
import com.friend.ui.components.AppOutlineTextField

@Composable
fun PasswordSection(
    text: TextInput,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    AppOutlineTextField(
        text = text.value,
        error = if (text.isDirty) stringResource(Res.string.error_invalid_password) else null,
        modifier = modifier.fillMaxWidth(),
        title = stringResource(Res.string.label_password),
        placeholder = stringResource(Res.string.hint_password),
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
        ),
        isPassword = true
    )
}