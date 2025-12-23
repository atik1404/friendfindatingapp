package com.friend.profile.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.typography.AppTypography
import com.friend.ui.components.AppText

@Composable
fun LabeledValue(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    alignment: Alignment.Horizontal = Alignment.Start,
    maxLines: Int = 1,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = alignment
    ) {
        AppText(
            text = title,
            fontWeight = FontWeight.Light,
            textStyle = AppTypography.bodySmall
        )
        Spacer(modifier = Modifier.height(SpacingToken.micro))
        AppText(
            text = value,
            fontWeight = FontWeight.Medium,
            maxLines = maxLines
        )
    }
}