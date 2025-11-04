package com.friend.chatlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.friend.designsystem.spacing.RadiusToken
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPaddingSymmetric
import com.friend.designsystem.theme.backgroundColors
import com.friend.designsystem.theme.textColors
import com.friend.designsystem.theme.textFieldColors
import com.friend.designsystem.typography.AppTypography
import com.friend.ui.common.AppToolbar
import com.friend.ui.components.AppBaseTextField
import com.friend.ui.components.AppScaffold
import com.friend.ui.components.AppText
import com.friend.ui.components.NetworkImageLoader
import com.friend.ui.preview.LightDarkPreview
import timber.log.Timber
import com.friend.designsystem.R as Res

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(
    onBackButtonClicked: () -> Unit,
    navigateToChatRoom: (String, String) -> Unit
) {
    AppScaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            AppToolbar(
                title = stringResource(Res.string.title_chat),
                onBackClick = {
                    onBackButtonClicked.invoke()
                })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .consumeWindowInsets(padding)
                .navigationBarsPadding()
                .imePadding()
                .appPaddingSymmetric(
                    horizontal = SpacingToken.extraSmall,
                    vertical = SpacingToken.medium
                )
        ) {
            SearchMenu()
            Spacer(modifier = Modifier.height(SpacingToken.medium))
            ChatList{
                navigateToChatRoom.invoke("Faysal","1234")
            }
        }
    }
}

@Composable
private fun SearchMenu() {
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

@Composable
private fun ChatListItem(modifier: Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = SpacingToken.micro)
            .background(
                MaterialTheme.backgroundColors.white,
                shape = RoundedCornerShape(RadiusToken.medium)
            )
            .appPaddingSymmetric(
                horizontal = SpacingToken.tiny,
                vertical = SpacingToken.medium
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        NetworkImageLoader(
            "https://images.unsplash.com/photo-1483909796554-bb0051ab60ad?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&q=80&w=2373",
            modifier = Modifier
                .size(50.dp),
            shape = CircleShape
        )

        Spacer(
            modifier = Modifier.width(SpacingToken.medium)
        )

        Column {
            AppText(
                text = "Atik Faysal",
                textStyle = AppTypography.titleMedium,
                fontWeight = FontWeight.Bold,
                textColor = MaterialTheme.textColors.primary,
            )

            Spacer(
                modifier = Modifier.height(SpacingToken.micro)
            )

            AppText(
                text = "Hey, How are you Atik?",
                textStyle = AppTypography.bodyMedium,
                fontWeight = FontWeight.Light,
                textColor = MaterialTheme.textColors.primary,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(
            modifier = Modifier.weight(1f)
        )

        AppText(
            text = "10:15 PM",
            textStyle = AppTypography.bodySmall,
            fontWeight = FontWeight.Medium,
            textColor = MaterialTheme.textColors.primary,
        )
    }
}

@Composable
private fun ChatList(
    onItemClicked: (String) -> Unit
) {
    LazyColumn {
        items(100, key = { it }) {
            ChatListItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable{
                        onItemClicked.invoke("")
                        Timber.e("item clicked")
                    }
            )
        }
    }
}

@Composable
@LightDarkPreview
private fun ScreenPreview() {
    ChatListScreen(
        onBackButtonClicked = {},
        navigateToChatRoom = { _, _ -> }
    )
}