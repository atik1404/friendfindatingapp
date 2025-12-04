package com.friend.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.friend.designsystem.spacing.RadiusToken
import com.friend.designsystem.R as Res

@Composable
fun LocalImageLoader(
    modifier: Modifier = Modifier,
    imageResId: Int,
    contentDescription: String = stringResource(Res.string.msg_image_content_description),
    contentScale: ContentScale = ContentScale.Crop,
) {
    Image(
        painter = painterResource(id = imageResId),
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = contentScale,
    )
}

@Composable
fun NetworkImageLoader(
    url: String,
    modifier: Modifier = Modifier,
    @DrawableRes placeholderRes: Int? = Res.drawable.image_loader,
    @DrawableRes errorRes: Int? = Res.drawable.image_loader,
    shape: Shape = RoundedCornerShape(RadiusToken.none),
    contentScale: ContentScale = ContentScale.Crop,
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .crossfade(true)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build(),
        contentDescription = stringResource(Res.string.msg_image_content_description),
        placeholder = placeholderRes?.let { painterResource(it) },
        error = errorRes?.let { painterResource(it) },
        contentScale = contentScale,
        modifier = modifier.clip(shape)
    )
}
