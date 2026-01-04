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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.friend.common.constant.AppConstants
import com.friend.common.dateparser.DateTimePatterns
import com.friend.common.dateparser.DateTimeParser
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
    hasMorePage: Boolean,
    onItemClicked: (ChatListItemApiEntity) -> Unit,
    onLoadMore: () -> Unit
) {
    val listState = rememberLazyListState()
    LaunchedEffect(listState, items, AppConstants.DATA_PER_PAGE) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisible ->
                val lastIndex = items.lastIndex
                if (lastVisible != null && lastVisible == lastIndex && hasMorePage && items.size >= AppConstants.DATA_PER_PAGE) {
                    onLoadMore.invoke()
                }
            }
    }
    LazyColumn(
        state = listState,
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

        Column(
            modifier = Modifier.weight(2f),
        ) {
            AppText(
                text = item.fullName,
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
        
        Spacer(modifier = Modifier.width(SpacingToken.medium))

        AppText(
            modifier = Modifier
                .weight(.5f)
                .align(alignment = Alignment.CenterVertically),
            text = DateTimeParser.parseToPattern(item.dateTime, DateTimePatterns.TIME_12_HM_AMPM),
            textStyle = AppTypography.bodySmall,
            fontWeight = FontWeight.Medium,
            textColor = MaterialTheme.textColors.primary,
        )
    }
}

@Composable
@LightPreview
private fun ScreenPreview() {
    ChatListItem(
        item = ChatListItemApiEntity(
            toUsername = "Atik Faysal",
            notificationToken = "",
            userImage = "",
            fullName = "Tom Cruise",
            lastMessage = "Hi, How are you?Hi, How are you?Hi, How are you?Hi, How are you?",
            dateTime = "2025-12-16"
        )
    )
}