package com.friend.registration.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.friend.designsystem.spacing.SpacingToken
import com.friend.domain.base.TextInput
import com.friend.registration.UiAction
import com.friend.ui.components.AppOutlineTextField
import com.friend.designsystem.R as Res

@Composable
fun NameSection(
    fullName: TextInput,
    email: TextInput,
    onFullNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AppOutlineTextField(
            text = fullName.value,
            error = if (fullName.isDirty) stringResource(Res.string.error_invalid_name) else null,
            modifier = modifier.weight(1f),
            title = stringResource(Res.string.label_full_name),
            placeholder = stringResource(Res.string.hint_full_name),
            onValueChange = onFullNameChange,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
            ),
        )

        Spacer(modifier = modifier.width(SpacingToken.medium))

        EmailSection(
            modifier = modifier.weight(1f),
            text = email,
            onValueChange = {
                onEmailChange.invoke(it)
            },
        )
    }
}
