package com.friend.chatroom.components.conversation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.friend.designsystem.spacing.IconSizeToken
import com.friend.designsystem.spacing.RadiusToken
import com.friend.designsystem.theme.surfaceColors
import com.friend.ui.components.AppIconButton
import com.friend.ui.components.NetworkImageLoader
import com.friend.ui.components.VideoThumbnailLoader

@Composable
fun ImageMessageContent(url: String, modifier: Modifier) {
    NetworkImageLoader(
        url = url,
        modifier = modifier,
        shape = RoundedCornerShape(RadiusToken.large)
    )
}

@Composable
fun VideoMessageContent(url: String, modifier: Modifier) {
    Box {
        VideoThumbnailLoader(
            videoUrl = url,
            modifier = modifier,
            shape = RoundedCornerShape(RadiusToken.large)
        )

        AppIconButton(
            modifier = Modifier
                .align(Alignment.Center),
            iconSize = IconSizeToken.mediumLarge,
            onClick = { /*TODO*/ },
            vectorIcon = Icons.Default.PlayCircle,
            tint = MaterialTheme.surfaceColors.primary.copy(alpha = .5f)
        )
    }
}


