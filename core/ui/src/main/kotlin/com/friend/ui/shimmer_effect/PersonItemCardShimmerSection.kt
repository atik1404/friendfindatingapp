package com.friend.ui.shimmer_effect

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPadding
import com.friend.designsystem.spacing.appPaddingSymmetric

@Composable
fun PersonItemCardShimmerSection(
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = modifier.appPadding(SpacingToken.micro)
    ) {
        // Image placeholder
        Box(
            modifier = Modifier
                .fillMaxSize()
                .height(200.dp)
                .clip(RoundedCornerShape(SpacingToken.extraSmall))
                .shimmer()
        )

        // Name label placeholder
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .appPaddingSymmetric(
                    horizontal = SpacingToken.medium,
                    vertical = SpacingToken.micro
                )
                .clip(RoundedCornerShape(SpacingToken.medium))
                .fillMaxWidth()        // approximate username width
                .height(20.dp)         // approximate text height
                .shimmer()
        )
    }
}
