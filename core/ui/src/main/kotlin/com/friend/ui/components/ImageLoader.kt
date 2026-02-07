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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
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

@OptIn(ExperimentalGlideComposeApi::class)
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
) {

    val placeholderPainter = placeholderRes?.let { painterResource(it) }

    Box(
        modifier = modifier.clip(shape),
        contentAlignment = Alignment.Center
    ) {
        when {
            url.isNotEmpty() -> {
                GlideImage(
                    model = url,
                    contentDescription = stringResource(Res.string.msg_image_content_description),
                    modifier = Modifier.matchParentSize(),
                    contentScale = contentScale,
                ) {
                    it
                        .placeholder(placeholderRes ?: 0)
                        .error(errorRes ?: 0)
                        .let { request ->
                            if (crossfade) request.transition(DrawableTransitionOptions.withCrossFade())
                            else request
                        }
                }
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

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun VideoThumbnailLoader(
    modifier: Modifier = Modifier,
    videoUrl: String,
    @DrawableRes placeholderRes: Int? = Res.drawable.image_loading_placeholder,
    @DrawableRes errorRes: Int? = Res.drawable.image_loading_placeholder,
    shape: Shape = RoundedCornerShape(RadiusToken.none),
    contentScale: ContentScale = ContentScale.Crop,
    crossfade: Boolean = false,
) {
    Box(
        modifier = modifier.clip(shape),
        contentAlignment = Alignment.Center
    ) {
        GlideImage(
            model = videoUrl,
            contentDescription = stringResource(Res.string.msg_image_content_description),
            modifier = modifier,
            contentScale = contentScale,
        ) {
            it.placeholder(placeholderRes ?: 0)
                .error(errorRes ?: 0)
                .frame(1000000)
                .let { request ->
                    if (crossfade) request.transition(DrawableTransitionOptions.withCrossFade())
                    else request
                }
        }
    }
}