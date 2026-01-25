package com.friend.chatroom.components

import android.graphics.Bitmap
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.friend.common.utils.FilesUtils.convertToFile
import com.friend.designsystem.spacing.IconSizeToken
import com.friend.designsystem.spacing.RadiusToken
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.StrokeTokens
import com.friend.designsystem.spacing.appPadding
import com.friend.designsystem.spacing.appPaddingHorizontal
import com.friend.designsystem.spacing.appPaddingSymmetric
import com.friend.designsystem.theme.backgroundColors
import com.friend.designsystem.theme.strokeColors
import com.friend.designsystem.theme.surfaceColors
import com.friend.designsystem.theme.textFieldColors
import com.friend.ui.common.CaptureImage
import com.friend.ui.components.AppBaseTextField
import com.friend.ui.components.AppIconButton
import com.friend.ui.components.BitmapImageLoader
import com.friend.ui.preview.LightPreview
import com.friend.ui.rememberPermissionAction
import timber.log.Timber
import java.io.File
import com.friend.designsystem.R as Res

@Composable
fun UserInputForm(
    modifier: Modifier,
    textMessage: String,
    isSendEnable: Boolean = false,
    onTextChange: (String) -> Unit,
    onClickAttachment: () -> Unit,
    onCaptureImage: (File) -> Unit,
    onSendTextMessage: (String) -> Unit,
    onStartRecording: () -> Unit,
    onStopRecording: () -> Unit,
    onCancelRecording: () -> Unit,
) {
    val swipeOffset = remember { mutableFloatStateOf(0f) }
    var isRecordingMessage by remember { mutableStateOf(false) }
    // New state to track if the recording is in "Locked/Hands-free" mode
    var isLocked by remember { mutableStateOf(false) }

    val withMicPermission = rememberPermissionAction(
        permission = android.Manifest.permission.RECORD_AUDIO,
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .appPaddingHorizontal(SpacingToken.tiny),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        AnimatedContent(
            targetState = isRecordingMessage,
            label = "text-field",
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
        ) { recording ->
            Box(Modifier.fillMaxWidth()) {
                if (recording) {
                    RecordingIndicator(
                        isLocked = isLocked,
                        onCancelRecording = {
                            isRecordingMessage = false
                            onCancelRecording.invoke()
                            isLocked = false
                        },
                        swipeOffset = {
                            swipeOffset.floatValue
                        }
                    )
                } else {
                    UserInputTextField(
                        textMessage = textMessage,
                        onTextChange = onTextChange,
                        onClickAttachment = onClickAttachment,
                        onCaptureImage = onCaptureImage
                    )
                }
            }
        }

        Spacer(Modifier.width(SpacingToken.tiny))

        if (!isSendEnable) {
            RecordButton(
                recording = isRecordingMessage,
                isLocked = isLocked,
                swipeOffset = { swipeOffset.floatValue },
                onSwipeOffsetChange = { offset -> swipeOffset.floatValue = offset },
                onStartRecording = {
                    val consumed = !isRecordingMessage
                    withMicPermission {
                        if (consumed) {
                            isRecordingMessage = true
                            onStartRecording.invoke()
                        }
                    }

                    consumed
                },
                onFinishRecording = {
                    isRecordingMessage = false
                    isLocked = false
                    onStopRecording.invoke()
                },
                onCancelRecording = {
                    isRecordingMessage = false
                    onCancelRecording.invoke()
                    isLocked = false
                },
                onLockRecording = {
                    isLocked = true
                },
                modifier = Modifier.fillMaxHeight(),
            )
        } else {
            AppIconButton(
                modifier = Modifier
                    .size(IconSizeToken.mediumLarge),
                onClick = {
                    onSendTextMessage.invoke(textMessage)
                },
                vectorIcon = Icons.Default.Send
            )
        }
    }
}

@Composable
private fun UserInputTextField(
    modifier: Modifier = Modifier,
    textMessage: String,
    onTextChange: (String) -> Unit,
    onClickAttachment: () -> Unit,
    onCaptureImage: (File) -> Unit,
) {
    val context = LocalContext.current
    Row(
        modifier = modifier
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
            modifier = modifier
                .weight(1f)
                .fillMaxHeight(),
            shape = RoundedCornerShape(SpacingToken.extraLarge),
            onValueChange = onTextChange,
            value = textMessage,
            placeholder = stringResource(Res.string.hint_write_something_here),
            colors = MaterialTheme.textFieldColors.transparentOutlinedTextField
        )

        AppIconButton(
            modifier = modifier.size(IconSizeToken.large),
            onClick = onClickAttachment,
            vectorIcon = Icons.Default.AttachFile
        )

        Spacer(modifier.width(SpacingToken.tiny))

        CaptureImage(
            onCaptured = {
                val file = it.convertToFile(context)
                file?.let { f ->
                    onCaptureImage.invoke(f)
                }
            },
            onError = {}
        ) {
            AppIconButton(
                modifier = modifier.size(IconSizeToken.large),
                onClick = it,
                vectorIcon = Icons.Default.CameraAlt
            )
        }
    }
}

@Composable
fun AttachedFilePreviewUi(
    modifier: Modifier = Modifier,
    bitmap: Bitmap? = null,
    clearFile: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .appPaddingSymmetric(SpacingToken.medium)
            .width(IconSizeToken.huge)
            .height(IconSizeToken.huge)
            .background(
                color = MaterialTheme.surfaceColors.white,
                shape = RoundedCornerShape(RadiusToken.medium)
            )
            .clip(RoundedCornerShape(RadiusToken.medium))
    ) {
        bitmap?.let {
            BitmapImageLoader(
                bitmap = it,
                modifier = modifier
                    .matchParentSize()
                    .background(
                        color = MaterialTheme.surfaceColors.white,
                        shape = RoundedCornerShape(RadiusToken.medium)
                    )
            )
        }

        AppIconButton(
            vectorIcon = Icons.Default.Clear,
            tint = MaterialTheme.surfaceColors.grayLight,
            onClick = clearFile,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(IconSizeToken.small) // tweak as you like
                .clip(CircleShape)
                .background(MaterialTheme.surfaceColors.primary)
                .border(
                    width = StrokeTokens.hairline,
                    color = MaterialTheme.strokeColors.primary,
                    shape = CircleShape
                )
                .appPadding(SpacingToken.micro) // inner padding for icon touch target
        )
    }
}

@Composable
@LightPreview
private fun ScreenPreview() {
    AttachedFilePreviewUi()
}

@Composable
@LightPreview
private fun InputBoxPreview() {
    UserInputForm(
        textMessage = "",
        onTextChange = {},
        onClickAttachment = {},
        onCaptureImage = {},
        onSendTextMessage = {},
        modifier = Modifier,
        isSendEnable = false,
        onStartRecording = {},
        onStopRecording = {},
        onCancelRecording = {}
    )
}