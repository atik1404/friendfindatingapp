package com.friend.ui.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.friend.designsystem.R as Res

@Composable
fun LoadLocalImage(
    modifier: Modifier = Modifier,
    imageResId: Int,
    contentDescription: String = stringResource(Res.string.msg_image_content_description),
    contentScale: ContentScale = ContentScale.Crop,
) {
    Image(
        painter = painterResource(id = imageResId),
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = contentScale
    )
}