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
fun EmailSection(
    text: String,
    isInvalid: Boolean = false,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    AppOutlineTextField(
        text = text,
        modifier = modifier.fillMaxWidth(),
        error = if (isInvalid) stringResource(Res.string.error_invalid_email) else null,
        title = stringResource(Res.string.label_email),
        placeholder = stringResource(Res.string.hint_email),
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
        ),
    )
}