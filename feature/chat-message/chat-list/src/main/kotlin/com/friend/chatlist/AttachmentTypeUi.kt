package com.friend.chatlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocalMovies
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.friend.designsystem.spacing.IconSizeToken
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.theme.buttonColors
import com.friend.designsystem.theme.surfaceColors
import com.friend.ui.components.AppIconButton
import com.friend.ui.preview.LightDarkPreview

@Composable
fun AttachmentTypeUi(
    modifier: Modifier = Modifier
) {
    Row() {
        AttachmentType(
            icon = Icons.Default.LocalMovies,
            onClick = {}
        )
        
        Spacer(modifier = Modifier.width(SpacingToken.tiny))


        AttachmentType(
            icon = Icons.Default.CameraAlt,
            onClick = {}
        )

        Spacer(modifier = Modifier.width(SpacingToken.tiny))

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
                color = MaterialTheme.buttonColors.primaryButton.containerColor,
                shape = CircleShape
            ),
        onClick = {},
        vectorIcon = icon
    )
}

@Composable
@LightDarkPreview
private fun ScreenPreview() {
    AttachmentTypeUi()
}