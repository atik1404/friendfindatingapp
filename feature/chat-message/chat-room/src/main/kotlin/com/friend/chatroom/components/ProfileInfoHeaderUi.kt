package com.friend.chatroom.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ForwardToInbox
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.friend.designsystem.spacing.IconSizeToken
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPadding
import com.friend.designsystem.theme.backgroundColors
import com.friend.designsystem.theme.surfaceColors
import com.friend.designsystem.theme.textColors
import com.friend.designsystem.typography.AppTypography
import com.friend.ui.components.AppIconButton
import com.friend.ui.components.AppPopupMenu
import com.friend.ui.components.AppText
import com.friend.ui.components.ChatRoomPopupMenu
import com.friend.ui.components.NetworkImageLoader
import com.friend.ui.components.PopupMenuType

@Composable
fun ProfileInfoHeader(
    modifier: Modifier,
    fullName: String,
    userImage: String,
    isBlocked: Boolean,
    isAdministrator: Boolean,
    backToChatListScreen: () -> Unit,
    onProfileImageClicked: () -> Unit,
    onMenuClicked: (PopupMenuType) -> Unit,
    isItemSelectionEnable: Boolean,
    onSelectionCancel: () -> Unit,
    onForwardMessage: () -> Unit,
    onDeleteMessage: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.backgroundColors.white)
            .appPadding(SpacingToken.extraSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppIconButton(onClick = backToChatListScreen, vectorIcon = Icons.Default.ArrowBackIosNew)

        NetworkImageLoader(
            url = userImage,
            name = fullName,
            modifier = Modifier
                .size(IconSizeToken.extraLarge)
                .clickable(onClick = onProfileImageClicked),
            shape = CircleShape
        )

        Spacer(modifier = Modifier.width(SpacingToken.medium))

        AppText(
            modifier = Modifier.weight(1f),
            text = fullName,
            textStyle = AppTypography.titleMedium,
            fontWeight = FontWeight.Bold,
            textColor = MaterialTheme.textColors.primary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.width(SpacingToken.small))

        if (!isBlocked && !isAdministrator) {
            if (isItemSelectionEnable) {
                Row {
                    AppIconButton(
                        vectorIcon = Icons.Default.ForwardToInbox,
                        onClick = onForwardMessage,
                        tint = MaterialTheme.surfaceColors.graySoft
                    )
                    AppIconButton(
                        vectorIcon = Icons.Default.Delete,
                        onClick = onDeleteMessage,
                        tint = MaterialTheme.surfaceColors.graySoft
                    )
                    AppIconButton(
                        vectorIcon = Icons.Default.Clear,
                        onClick = onSelectionCancel,
                        tint = MaterialTheme.surfaceColors.graySoft
                    )
                }
            } else {
                AppPopupMenu(
                    icon = Icons.Default.MoreVert,
                    menuItems = ChatRoomPopupMenu,
                    onClick = onMenuClicked
                )
            }
        }
    }
}
