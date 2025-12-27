package com.friend.ui.common

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.friend.common.utils.ImageUtils

@Composable
fun CaptureImage(
    onCaptured: (Uri) -> Unit,
    onError: () -> Unit,
    captureButton: @Composable (onClick: () -> Unit) -> Unit,
) {
    val context = LocalContext.current
    var photoUri by remember { mutableStateOf<Uri?>(null) }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            photoUri?.let(onCaptured)?.run { onError.invoke() }
        } else onError.invoke()
    }

    captureButton {
        val uri = ImageUtils.createImageUri(context)
        photoUri = uri
        takePictureLauncher.launch(uri)
    }
}

@Composable
fun GalleryImagPicker(
    onImageSelected: (Uri) -> Unit,
    onError: () -> Unit,
    pickerButton: @Composable (onClick: () -> Unit) -> Unit,
) {
    val pickMedia =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                onImageSelected(uri)
            } else onError.invoke()
        }

    pickerButton {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
}