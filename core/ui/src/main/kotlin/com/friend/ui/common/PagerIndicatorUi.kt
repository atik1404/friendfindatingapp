package com.friend.ui.common

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.friend.designsystem.spacing.IconSizeToken
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.theme.surfaceColors

@Composable
fun PageIndicator(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier,
    selectedColor: Color = MaterialTheme.surfaceColors.primary,
    unselectedColor: Color = MaterialTheme.surfaceColors.secondary
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        repeat(pageCount) { iteration ->
            val isSelected = currentPage == iteration

            // Animate width for a "worm" or "stretch" effect
            val width by animateDpAsState(
                targetValue = IconSizeToken.mediumLarge,
                label = "dot_width"
            )

            val color = if (isSelected) selectedColor else unselectedColor

            Box(
                modifier = Modifier
                    .padding(SpacingToken.micro)
                    .height(SpacingToken.tiny)
                    .width(width) // Animated width
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}