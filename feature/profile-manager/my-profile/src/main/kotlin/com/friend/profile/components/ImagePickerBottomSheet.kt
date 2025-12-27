package com.friend.profile.components

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.friend.common.utils.ImageUtils.convertToFile
import com.friend.designsystem.spacing.IconSizeToken
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPaddingHorizontal
import com.friend.designsystem.spacing.appPaddingVertical
import com.friend.ui.common.CaptureImage
import com.friend.ui.common.GalleryImagPicker
import com.friend.ui.common.ShowBottomSheet
import com.friend.ui.components.AppText
import com.friend.ui.preview.LightPreview
import java.io.File
import com.friend.designsystem.R as Res

@Composable
fun ImagePickerBottomSheet(
    isVisible: Boolean = false,
    onDismissRequest: () -> Unit,
    onError: () -> Unit,
    onImageSelected: (Uri) -> Unit
) {

    if (isVisible)
        ShowBottomSheet(
            title = "Choose image",
            onDismissRequest = onDismissRequest
        ) {
            ImagePickerUiSection(
                onImageSelected = {
                    onImageSelected.invoke(it)
                    onDismissRequest.invoke()
                },
                onError = {
                    onDismissRequest.invoke()
                    onError.invoke()
                }
            )
        }
}

@Composable
private fun ImagePickerUiSection(
    onImageSelected: (Uri) -> Unit,
    onError: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .appPaddingVertical(SpacingToken.large)
            .appPaddingHorizontal(SpacingToken.huge),
        horizontalArrangement = Arrangement.SpaceAround
    ) {

        CaptureImage(
            onCaptured = {
                onImageSelected.invoke(it)
            },
            onError = onError
        ) {
            ImagePicker(
                icon = Res.drawable.ic_camera,
                title = Res.string.action_camera,
                onClick = it
            )
        }

        GalleryImagPicker(
            onImageSelected = {
                onImageSelected.invoke(it)
            },
            onError = onError
        ) {
            ImagePicker(
                icon = Res.drawable.ic_gallery,
                title = Res.string.action_gallery,
                onClick = it
            )
        }

    }
}

@Composable
private fun ImagePicker(
    modifier: Modifier = Modifier,
    icon: Int,
    title: Int,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier.clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier.size(IconSizeToken.mediumLarge),
            painter = painterResource(id = icon),
            contentDescription = stringResource(Res.string.msg_image_content_description)
        )
        Spacer(modifier.height(SpacingToken.medium))
        AppText(
            stringResource(title),
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
@LightPreview
private fun ScreenPreview() {
    ImagePickerBottomSheet(
        isVisible = true,
        onDismissRequest = {},
        onImageSelected = {},
        onError = {}
    )
}