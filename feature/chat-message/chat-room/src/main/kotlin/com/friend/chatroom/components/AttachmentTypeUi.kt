package com.friend.chatroom.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.ui.platform.LocalContext
import com.friend.common.utils.FilesUtils.convertToFile
import com.friend.designsystem.spacing.IconSizeToken
import com.friend.designsystem.spacing.RadiusToken
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPadding
import com.friend.designsystem.spacing.appPaddingSymmetric
import com.friend.designsystem.theme.backgroundColors
import com.friend.designsystem.theme.buttonColors
import com.friend.ui.common.CaptureImage
import com.friend.ui.common.ImageFilePicker
import com.friend.ui.common.VideoFilePicker
import com.friend.ui.components.AppIconButton
import com.friend.ui.preview.LightPreview
import java.io.File

@Composable
fun AnimatedAttachmentType(
    visible: Boolean,
    modifier: Modifier = Modifier,
    pickImage: (File) -> Unit,
    pickVideo: (File?) -> Unit,
) {
    AnimatedVisibility(
        visible = visible,
        modifier = modifier,
        enter = slideInVertically(
            initialOffsetY = { fullHeight -> fullHeight }
        ) + fadeIn(),
        exit = slideOutVertically(
            targetOffsetY = { fullHeight -> fullHeight }
        ) + fadeOut()
    ) {
        AttachmentTypeUi(
            pickImage = pickImage,
            pickVideo = {
                pickVideo.invoke(it)
            },
        )
    }
}

@Composable
fun AttachmentTypeUi(
    modifier: Modifier = Modifier,
    pickImage: (File) -> Unit,
    pickVideo: (File) -> Unit,
) {
    val context = LocalContext.current
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
        VideoFilePicker(
            onFileSelected = {
                val file = it.convertToFile(context)
                file?.let { f ->
                    pickVideo.invoke(f)
                }
            },
            onError = {}
        ) {
            AttachmentType(
                icon = Icons.Default.LocalMovies,
                onClick = it
            )
        }

        Spacer(modifier = Modifier.width(SpacingToken.small))

        CaptureImage(
            onCaptured = {
                val file = it.convertToFile(context)
                file?.let { f ->
                    pickImage.invoke(f)
                }
            },
            onError = {}
        ) {
            AttachmentType(
                icon = Icons.Default.CameraAlt,
                onClick = it
            )
        }

        Spacer(modifier = Modifier.width(SpacingToken.small))

        ImageFilePicker(
            onImageSelected = {
                val file = it.convertToFile(context)
                file?.let { f ->
                    pickImage.invoke(f)
                }
            },
            onError = {}
        ) {
            AttachmentType(
                icon = Icons.Default.Image,
                onClick = it
            )
        }
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
@LightPreview
private fun ScreenPreview() {
    AttachmentTypeUi(
        pickImage = {},
        pickVideo = {},
    )
}