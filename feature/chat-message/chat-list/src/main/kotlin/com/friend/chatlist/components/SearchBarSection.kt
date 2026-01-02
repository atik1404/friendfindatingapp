package com.friend.chatlist.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.theme.textFieldColors
import com.friend.ui.components.AppBaseTextField
import com.friend.designsystem.R as Res

@Composable
fun SearchBarSection(
    searchKeyword: String,
    onSearchKeywordChange: (String) -> Unit,
) {
    AppBaseTextField(
        value = searchKeyword,
        modifier = Modifier.fillMaxWidth(),
        placeholder = stringResource(Res.string.hint_search_here),
        onValueChange = { onSearchKeywordChange.invoke(it) },
        colors = MaterialTheme.textFieldColors.outlinedTextField,
        shape = RoundedCornerShape(SpacingToken.medium),
        trailingIcon = if (searchKeyword.isEmpty()) Icons.Default.Search else Icons.Default.Clear,
        onTrailingClick = {
            if (searchKeyword.isNotEmpty())
                onSearchKeywordChange.invoke("")
        }
    )
}