package com.friend.chatroom.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.friend.designsystem.R as Res
import com.friend.designsystem.spacing.IconSizeToken
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPaddingHorizontal
import com.friend.designsystem.theme.backgroundColors
import com.friend.designsystem.theme.buttonColors
import com.friend.designsystem.theme.textFieldColors
import com.friend.ui.components.AppBaseTextField
import com.friend.ui.components.AppIconButton

@Composable
fun MessageSendUi(
    modifier: Modifier,
    onClickAttachment: () -> Unit
) {
    var message by remember { mutableStateOf("") }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .appPaddingHorizontal(SpacingToken.tiny),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .height(IntrinsicSize.Min)
                .background(
                    color = MaterialTheme.backgroundColors.white,
                    shape = RoundedCornerShape(SpacingToken.extraLarge)
                )
                .appPaddingHorizontal(SpacingToken.tiny),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppBaseTextField(
                singleLine = false,
                maxLines = 3,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                shape = RoundedCornerShape(SpacingToken.extraLarge),
                onValueChange = { message = it },
                value = message,
                placeholder = stringResource(Res.string.hint_write_something_here),
                colors = MaterialTheme.textFieldColors.transparentOutlinedTextField
            )

            AppIconButton(
                modifier = Modifier.size(IconSizeToken.large),
                onClick = onClickAttachment,
                vectorIcon = Icons.Default.AttachFile
            )

            Spacer(Modifier.width(SpacingToken.tiny))

            AppIconButton(
                modifier = Modifier.size(IconSizeToken.large),
                onClick = {},
                vectorIcon = Icons.Default.CameraAlt
            )
        }

        Spacer(Modifier.width(SpacingToken.tiny))

        if (message.isEmpty()) {
            AppIconButton(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.buttonColors.primaryButton.disabledContainerColor,
                        shape = CircleShape
                    )
                    .size(IconSizeToken.mediumLarge),
                onClick = {},
                vectorIcon = Icons.Default.Mic
            )
        } else {
            AppIconButton(
                modifier = Modifier
                    .size(IconSizeToken.mediumLarge),
                onClick = {},
                vectorIcon = Icons.Default.Send
            )
        }
    }
}