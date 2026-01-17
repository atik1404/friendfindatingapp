package com.friend.chatroom.components.conversation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PauseCircleFilled
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.friend.common.dateparser.DateTimeParser
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPaddingHorizontal
import com.friend.designsystem.theme.surfaceColors
import com.friend.ui.common.RoundedLinearProgressWithThumb
import com.friend.ui.components.AppIconButton
import com.friend.ui.components.AppText

@Composable
fun AudioMessageContent(
    id: String,
    url: String,
    duration: Long,
   // audioController: AudioPlayerController
) {
    val maxMillis = duration.coerceAtLeast(0L)
    //val state = audioController.state.collectAsState().value

    val isThisActive = false
    val isPlaying = false
    val positionMs =  0L

    Row(
        modifier = Modifier.appPaddingHorizontal(SpacingToken.medium),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AppIconButton(
            modifier = Modifier.background(
                color = MaterialTheme.surfaceColors.white.copy(alpha = .1f),
                shape = CircleShape
            ),
            vectorIcon = if (isPlaying) Icons.Default.PauseCircleFilled
            else Icons.Default.PlayCircleFilled,
            onClick = {  }
        )

        Spacer(modifier = Modifier.width(SpacingToken.small))

        RoundedLinearProgressWithThumb(
            modifier = Modifier.weight(1f),
            progress = positionMs.coerceAtMost(maxMillis).toFloat(),
            max = maxMillis.toFloat()
        )

        Spacer(modifier = Modifier.width(SpacingToken.small))

        AppText(text = DateTimeParser.convertMillisToTime(duration))
    }
}