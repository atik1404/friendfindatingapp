package com.friend.chatlist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.friend.common.dateparser.DateTimePatterns
import com.friend.common.dateparser.DateTimeUtils
import com.friend.designsystem.spacing.RadiusToken
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPaddingSymmetric
import com.friend.designsystem.theme.backgroundColors
import com.friend.designsystem.theme.textColors
import com.friend.designsystem.typography.AppTypography
import com.friend.entity.chatmessage.ChatListItemApiEntity
import com.friend.ui.components.AppText
import com.friend.ui.components.NetworkImageLoader
import com.friend.ui.preview.LightPreview

@Composable
fun ChatListSection(
    modifier: Modifier = Modifier,
    items: List<ChatListItemApiEntity>,
    onItemClicked: (ChatListItemApiEntity) -> Unit,
    onLoadMore: () -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth()
    ) {
        items(
            items = items,
            key = { it.toUsername }
        ) { item ->
            ChatListItem(
                item = item,
                modifier = modifier
                    .fillMaxWidth()
                    .clickable {
                        onItemClicked.invoke(item)
                    }
            )
        }
    }
}

@Composable
private fun ChatListItem(
    modifier: Modifier = Modifier,
    item: ChatListItemApiEntity
) {
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
            url = item.userImage,
            name = item.toUsername,
            modifier = Modifier
                .size(50.dp),
            shape = CircleShape
        )

        Spacer(
            modifier = Modifier.width(SpacingToken.medium)
        )

        Column {
            AppText(
                text = item.toUsername,
                textStyle = AppTypography.titleMedium,
                fontWeight = FontWeight.Bold,
                textColor = MaterialTheme.textColors.primary,
            )

            Spacer(
                modifier = Modifier.height(SpacingToken.micro)
            )

            AppText(
                text = item.lastMessage,
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
            text = DateTimeUtils.parseToPattern(item.dateTime, DateTimePatterns.TIME_12_HM_AMPM),
            textStyle = AppTypography.bodySmall,
            fontWeight = FontWeight.Medium,
            textColor = MaterialTheme.textColors.primary,
        )
    }
}

@Composable
@LightPreview
private fun ScreenPreview() {
    ChatListSection(
        items = listOf(
            ChatListItemApiEntity(
                toUsername = "Tom Cruise",
                notificationToken = "",
                userImage = "",
                fullName = "Tom Cruise",
                lastMessage = "Hi, How are you?",
                dateTime = "2025-12-16T10:25:47Z"
            ), ChatListItemApiEntity(
                toUsername = "Tom Cruise",
                notificationToken = "",
                userImage = "",
                fullName = "Tom Cruise",
                lastMessage = "Hi, How are you?",
                dateTime = "2025-12-16T10:25:47Z"
            )
        ),
        onItemClicked = {},
        onLoadMore = {},
    )
}