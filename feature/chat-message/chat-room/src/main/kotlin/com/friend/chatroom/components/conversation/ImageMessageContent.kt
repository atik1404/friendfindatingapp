package com.friend.chatroom.components.conversation

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.friend.designsystem.spacing.RadiusToken
import com.friend.ui.components.NetworkImageLoader

@Composable
fun ImageMessageContent(url: String, modifier: Modifier) {
    NetworkImageLoader(
        url = url,
        modifier = modifier,
        shape = RoundedCornerShape(RadiusToken.large)
    )
}
