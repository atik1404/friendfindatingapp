package com.friend.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.friend.designsystem.R as Res
import com.friend.designsystem.theme.backgroundColors
import com.friend.ui.preview.LightPreview

@Composable
fun LoadingUi(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.backgroundColors.primary.copy(alpha = .7f))
            .clickable(
                enabled = true,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { /* no-op, just block */ }
    ) {
        LoadingAnimation(
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}

@Composable
fun LoadingAnimation(
    modifier: Modifier = Modifier
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(Res.raw.anim_loading)
    )

    LottieAnimation(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        modifier = modifier
            .size(100.dp)
    )
}

@Composable
@LightPreview
private fun ScreenPreview() {
    LoadingUi()
}