package com.friend.chatroom.components.messageUi

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.friend.designsystem.spacing.RadiusToken
import com.friend.ui.components.NetworkImageLoader

@Composable
fun ImageMessageContent(url: String) {
    NetworkImageLoader(
        url = url,
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        shape = RoundedCornerShape(RadiusToken.large)
    )
}
