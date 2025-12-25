package com.friend.chatroom.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.friend.designsystem.spacing.IconSizeToken
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPadding
import com.friend.designsystem.theme.backgroundColors
import com.friend.designsystem.theme.textColors
import com.friend.designsystem.typography.AppTypography
import com.friend.ui.components.AppPopupMenu
import com.friend.ui.components.AppText
import com.friend.ui.components.ChatRoomPopupMenu
import com.friend.ui.components.NetworkImageLoader
import com.friend.ui.components.PopupMenuType

@Composable
fun ProfileInfoHeader(
    modifier: Modifier,
    username: String,
    backToChatListScreen: () -> Unit,
    onMenuClicked: (PopupMenuType) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.backgroundColors.white)
            .appPadding(SpacingToken.extraSmall),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = {
                backToChatListScreen.invoke()
            }
        ) {
            Icon(
                contentDescription = "",
                imageVector = Icons.Default.ArrowBackIosNew,
            )
        }

        NetworkImageLoader(
            url = "https://images.mubicdn.net/images/cast_member/2184/cache-2992-1547409411/image-w856.jpg",
            modifier = Modifier
                .size(IconSizeToken.extraLarge),
            shape = CircleShape
        )

        Spacer(
            modifier = Modifier.width(SpacingToken.medium)
        )

        AppText(
            text = username,
            textStyle = AppTypography.titleMedium,
            fontWeight = FontWeight.Bold,
            textColor = MaterialTheme.textColors.primary,
        )

        Spacer(
            modifier = Modifier.weight(1f)
        )

        AppPopupMenu(
            icon = Icons.Default.MoreVert,
            menuItems = ChatRoomPopupMenu,
            onClick = {
                onMenuClicked.invoke(it)
            }
        )
    }
}