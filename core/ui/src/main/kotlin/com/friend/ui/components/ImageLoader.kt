package com.friend.ui.components

import android.graphics.Bitmap
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.video.VideoFrameDecoder
import coil3.video.videoFrameMicros
import com.friend.common.extfun.initialsOf
import com.friend.designsystem.spacing.RadiusToken
import com.friend.designsystem.theme.surfaceColors
import com.friend.designsystem.theme.textColors
import com.friend.designsystem.typography.AppTypography
import com.friend.designsystem.R as Res

@Composable
fun ResourceImageLoader(
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
    modifier: Modifier = Modifier,
    url: String,
    name: String? = null,
    isLoading: Boolean = false,
    @DrawableRes placeholderRes: Int? = Res.drawable.image_loading_placeholder,
    @DrawableRes errorRes: Int? = Res.drawable.image_loading_placeholder,
    shape: Shape = RoundedCornerShape(RadiusToken.none),
    contentScale: ContentScale = ContentScale.Crop,
    crossfade: Boolean = false,
    memoryCachePolicy: CachePolicy = CachePolicy.ENABLED,
    diskCachePolicy: CachePolicy = CachePolicy.ENABLED,
) {
    val context = LocalContext.current
    val imageLoader = remember(context) { SingletonImageLoader.get(context) }

    val placeholderPainter = placeholderRes?.let { painterResource(it) }
    val errorPainter = errorRes?.let { painterResource(it) }

    val request = remember(url, crossfade, memoryCachePolicy, diskCachePolicy) {
        if (url.isBlank()) null
        else ImageRequest.Builder(context)
            .data(url)
            .crossfade(crossfade)
            .memoryCachePolicy(memoryCachePolicy)
            .diskCachePolicy(diskCachePolicy)
            .build()
    }

    Box(
        modifier = modifier.clip(shape),
        contentAlignment = Alignment.Center
    ) {
        when {
            request != null -> {
                AsyncImage(
                    model = request,
                    imageLoader = imageLoader,
                    contentDescription = stringResource(Res.string.msg_image_content_description),
                    placeholder = placeholderPainter,
                    error = errorPainter,
                    contentScale = contentScale,
                    modifier = Modifier.matchParentSize()
                )
            }

            !name.isNullOrBlank() -> {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            MaterialTheme.surfaceColors.tertiary.copy(alpha = 0.25f),
                            shape = shape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    AppText(
                        text = name.initialsOf(),
                        textStyle = AppTypography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        textColor = MaterialTheme.textColors.primary,
                        alignment = TextAlign.Center
                    )
                }
            }

            else -> {
                placeholderPainter?.let {
                    Image(
                        painter = it,
                        contentDescription = null,
                        contentScale = contentScale,
                        modifier = Modifier.matchParentSize()
                    )
                }
            }
        }

        if (isLoading) CircularProgressIndicator()
    }
}


@Composable
fun BitmapImageLoader(
    modifier: Modifier = Modifier,
    bitmap: Bitmap,
    contentDescription: String = stringResource(Res.string.msg_image_content_description),
    contentScale: ContentScale = ContentScale.Crop,
) {
    Image(
        bitmap = bitmap.asImageBitmap(),
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = contentScale,
    )
}

@Composable
fun VideoThumbnailLoader(
    modifier: Modifier = Modifier,
    videoUrl: String,
    @DrawableRes placeholderRes: Int? = Res.drawable.image_loading_placeholder,
    @DrawableRes errorRes: Int? = Res.drawable.image_loading_placeholder,
    shape: Shape = RoundedCornerShape(RadiusToken.none),
    contentScale: ContentScale = ContentScale.Crop,
) {
    val context = LocalContext.current

    val imageLoader = remember {
        ImageLoader.Builder(context)
            .components { add(VideoFrameDecoder.Factory()) }
            .build()
    }

    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(videoUrl)
            .crossfade(true)
            .videoFrameMicros(1000000)
            .build(),
        imageLoader = imageLoader,
        contentDescription = stringResource(Res.string.msg_image_content_description),
        placeholder = placeholderRes?.let { painterResource(it) },
        error = errorRes?.let { painterResource(it) },
        contentScale = contentScale,
        modifier = modifier
            .clip(shape)
    )
}