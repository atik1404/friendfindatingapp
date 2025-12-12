package com.friend.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.friend.designsystem.theme.backgroundColors

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
        CircularProgressIndicator(
            modifier = modifier.align(Alignment.Center)
        )
    }
}