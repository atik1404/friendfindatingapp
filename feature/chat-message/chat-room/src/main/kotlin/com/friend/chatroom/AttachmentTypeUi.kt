package com.friend.chatroom

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocalMovies
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.friend.designsystem.spacing.IconSizeToken
import com.friend.designsystem.spacing.RadiusToken
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPadding
import com.friend.designsystem.spacing.appPaddingSymmetric
import com.friend.designsystem.theme.backgroundColors
import com.friend.designsystem.theme.buttonColors
import com.friend.ui.components.AppIconButton
import com.friend.ui.preview.LightDarkPreview

@Composable
fun AttachmentTypeUi(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .appPadding(SpacingToken.medium)
            .background(
                color = MaterialTheme.backgroundColors.white,
                shape = RoundedCornerShape(RadiusToken.medium)
            )
            .appPaddingSymmetric(
                horizontal = SpacingToken.medium,
                vertical = SpacingToken.small
            ),
        horizontalArrangement = Arrangement.Center
    ) {
        AttachmentType(
            icon = Icons.Default.LocalMovies,
            onClick = {}
        )
        
        Spacer(modifier = Modifier.width(SpacingToken.small))

        AttachmentType(
            icon = Icons.Default.CameraAlt,
            onClick = {}
        )

        Spacer(modifier = Modifier.width(SpacingToken.small))

        AttachmentType(
            icon = Icons.Default.Image,
            onClick = {}
        )
    }
}

@Composable
private fun AttachmentType(
    onClick: () -> Unit,
    icon: ImageVector
) {
    AppIconButton(
        modifier = Modifier
            .size(IconSizeToken.extraLarge)
            .background(
                color = MaterialTheme.buttonColors.primaryButton.disabledContainerColor,
                shape = CircleShape
            ),
        onClick = onClick,
        vectorIcon = icon
    )
}

@Composable
@LightDarkPreview
private fun ScreenPreview() {
    AttachmentTypeUi()
}