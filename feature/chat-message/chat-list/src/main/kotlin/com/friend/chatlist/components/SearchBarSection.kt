package com.friend.chatlist.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.friend.designsystem.R as Res
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.theme.textFieldColors
import com.friend.ui.components.AppBaseTextField

@Composable
fun SearchBarSection() {
    var searchKeyword by rememberSaveable { mutableStateOf("") }
    AppBaseTextField(
        value = searchKeyword,
        modifier = Modifier.fillMaxWidth(),
        placeholder = stringResource(Res.string.hint_search_here),
        onValueChange = { searchKeyword = it },
        colors = MaterialTheme.textFieldColors.outlinedTextField,
        shape = RoundedCornerShape(SpacingToken.medium),
        trailingIcon = Icons.Default.Search,
    )
}