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
import com.friend.ui.components.AppOutlineTextField
import com.friend.designsystem.R as Res

@Composable
fun NameSection(
    fullName: String,
    userName: String,
    isInvalidUserName: Boolean,
    onFullNameChange: (String) -> Unit,
    onUserNameChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AppOutlineTextField(
            text = userName,
            error = if(isInvalidUserName) stringResource(Res.string.error_invalid_username) else null,
            modifier = modifier.weight(1f),
            title = stringResource(Res.string.label_username),
            placeholder = stringResource(Res.string.hint_user_name),
            onValueChange = onUserNameChange,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
            ),
        )

        Spacer(modifier = modifier.width(SpacingToken.medium))

        AppOutlineTextField(
            text = fullName,
            modifier = modifier.weight(1f),
            title = stringResource(Res.string.label_full_name),
            placeholder = stringResource(Res.string.hint_full_name),
            onValueChange = onFullNameChange,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
            ),
        )
    }
}
