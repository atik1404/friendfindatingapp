package com.friend.ui.components

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.friend.common.extfun.initialsOf
import com.friend.designsystem.spacing.RadiusToken
import com.friend.designsystem.theme.surfaceColors
import com.friend.designsystem.theme.textColors
import com.friend.designsystem.typography.AppTypography
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
    modifier: Modifier = Modifier,
    url: String,
    name: String? = null,
    isLoading: Boolean = false,
    @DrawableRes placeholderRes: Int? = Res.drawable.image_loader,
    @DrawableRes errorRes: Int? = Res.drawable.image_loader,
    shape: Shape = RoundedCornerShape(RadiusToken.none),
    contentScale: ContentScale = ContentScale.Crop,
) {
    Box(
        modifier = modifier
            .clip(shape),
        contentAlignment = Alignment.Center
    ) {
        when {
            url.isNotEmpty() -> {
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
                // Optional: fallback placeholder background (if no url + no name)
                placeholderRes?.let {
                    Image(
                        painter = painterResource(it),
                        contentDescription = null,
                        contentScale = contentScale,
                        modifier = Modifier.matchParentSize()
                    )
                }
            }
        }

        if (isLoading) {
            CircularProgressIndicator()
        }
    }
}
