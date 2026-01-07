package com.friend.chatroom.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import com.friend.designsystem.R as Res

@Composable
fun SearchBarUi(
    modifier: Modifier = Modifier,
    onCancelClicked: () -> Unit,
    onSearchApply: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        snapshotFlow { text }
            .debounce(800)
            .map { it.trim() }
            .distinctUntilChanged()
            .collectLatest { query ->
                if (query.isNotBlank()) onSearchApply(query)
            }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.backgroundColors.white)
            .appPadding(SpacingToken.extraSmall),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AppBaseTextField(
            value = text,
            modifier = Modifier.weight(1f),
            placeholder = stringResource(Res.string.hint_search_here),
            onValueChange = { text = it },
            colors = MaterialTheme.textFieldColors.outlinedTextField,
            shape = RoundedCornerShape(SpacingToken.medium),
            keyboardActions = KeyboardActions {
                ImeAction.Search
            }
        )

        AppTextButton(
            text = stringResource(Res.string.action_cancel),
            onClick = {
                text = ""
                onCancelClicked.invoke()
            },
        )
    }
}