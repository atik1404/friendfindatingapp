package com.friend.ui.common

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import com.friend.designsystem.spacing.SpacingToken
import kotlin.math.min

@Composable
fun RoundedLinearProgressWithThumb(
    progress: Float,
    max: Float,
    modifier: Modifier = Modifier,
    trackHeight: Dp = SpacingToken.extraSmall,
    thumbRadius: Dp = SpacingToken.small,
    trackColor: Color = Color(0xFFE0E0E0),
    progressColor: Color = Color(0xFF3F51B5),
    thumbColor: Color = progressColor,
    thumbBorderColor: Color = Color.White,
    thumbBorderWidth: Dp = SpacingToken.minimum,
    animate: Boolean = true,
) {
    val ratio = if (max <= 0f) 0f else (progress / max).coerceIn(0f, 1f)

    val animated by animateFloatAsState(targetValue = ratio, label = "progress")
    val p = if (animate) animated else ratio

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(maxOf(trackHeight, thumbRadius * 2))
            .semantics {
                progressBarRangeInfo = ProgressBarRangeInfo(current = p, range = 0f..1f)
            }
    ) {
        val barH = trackHeight.toPx()
        val y = (size.height - barH) / 2f
        val trackRadius = barH / 2f

        val progressW = size.width * p
        val progressRadius = min(trackRadius, progressW / 2f)

        // Track
        drawRoundRect(
            color = trackColor,
            topLeft = Offset(0f, y),
            size = Size(size.width, barH),
            cornerRadius = CornerRadius(trackRadius, trackRadius)
        )

        // Progress
        drawRoundRect(
            color = progressColor,
            topLeft = Offset(0f, y),
            size = Size(progressW, barH),
            cornerRadius = CornerRadius(progressRadius, progressRadius)
        )

        // Thumb
        val r = thumbRadius.toPx()
        val cx = progressW.coerceIn(r, size.width - r)
        val cy = size.height / 2f

        drawCircle(color = thumbBorderColor, radius = r, center = Offset(cx, cy))
        drawCircle(
            color = thumbColor,
            radius = (r - thumbBorderWidth.toPx()).coerceAtLeast(0f),
            center = Offset(cx, cy)
        )
    }
}
