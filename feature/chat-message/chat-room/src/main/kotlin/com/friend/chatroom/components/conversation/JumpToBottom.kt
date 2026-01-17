package com.friend.chatroom.components.conversation

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.friend.designsystem.spacing.SpacingToken
import com.friend.ui.components.AppText
import com.friend.ui.preview.LightPreview
import com.friend.designsystem.R as Res


/**
 * Shows a button that lets the user scroll to the bottom.
 */
@Composable
fun JumpToBottom(enabled: Boolean, onClicked: () -> Unit, modifier: Modifier = Modifier) {
    // Show Jump to Bottom button
    val transition = updateTransition(
        enabled,
        label = "JumpToBottom visibility animation",
    )
    val bottomOffset by transition.animateDp(label = "JumpToBottom offset animation") {
        if (!it) {
            (-100).dp
        } else {
            100.dp
        }
    }
    if (bottomOffset > 0.dp) {
        ExtendedFloatingActionButton(
            icon = {
                Icon(
                    painter = painterResource(id = Res.drawable.ic_scroll_down),
                    modifier = Modifier.height(SpacingToken.medium),
                    contentDescription = null,
                )
            },
            text = {
                AppText(text = stringResource(Res.string.msg_jump_to_bottom))
            },
            onClick = onClicked,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary,
            modifier = modifier
                .offset(x = 0.dp, y = -bottomOffset)
                .height(SpacingToken.extraLarge),
        )
    }
}

@LightPreview
@Composable
fun JumpToBottomPreview() {
    JumpToBottom(enabled = true, onClicked = {})
}
