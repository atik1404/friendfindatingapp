package com.friend.chatroom.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPadding
import com.friend.designsystem.theme.backgroundColors
import com.friend.designsystem.theme.textFieldColors
import com.friend.ui.components.AppBaseTextField
import com.friend.ui.components.AppTextButton
import com.friend.designsystem.R as Res

@Composable
fun SearchBarUi(
    modifier : Modifier = Modifier,
    onCancelClicked: () -> Unit
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.backgroundColors.white)
            .appPadding(SpacingToken.extraSmall),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        var searchKeyword by rememberSaveable { mutableStateOf("") }
        AppBaseTextField(
            value = searchKeyword,
            modifier = Modifier.weight(1f),
            placeholder = stringResource(Res.string.hint_search_here),
            onValueChange = { searchKeyword = it },
            colors = MaterialTheme.textFieldColors.outlinedTextField,
            shape = RoundedCornerShape(SpacingToken.medium),
            keyboardActions = KeyboardActions {
                ImeAction.Search
            }
        )

        AppTextButton(
            text = stringResource(Res.string.action_cancel),
            onClick = onCancelClicked,
        )
    }
}