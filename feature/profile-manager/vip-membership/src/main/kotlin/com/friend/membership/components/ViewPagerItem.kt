package com.friend.membership.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPadding
import com.friend.designsystem.spacing.appPaddingHorizontal
import com.friend.designsystem.spacing.appPaddingVertical
import com.friend.designsystem.theme.surfaceColors
import com.friend.designsystem.typography.AppTypography
import com.friend.ui.components.AppText
import com.friend.ui.components.LocalImageLoader

@Composable
fun ViewPagerItem(
    modifier: Modifier,
    image: Int,
    title: String,
    description: String,
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.surfaceColors.white,
            contentColor = CardDefaults.cardColors().contentColor,
        )
    ) {
        Column(
            modifier = modifier
                .appPaddingVertical(SpacingToken.huge)
                .appPaddingHorizontal(SpacingToken.small),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LocalImageLoader(
                imageResId = image,
                modifier = modifier
                    .fillMaxWidth()
                    .height(220.dp),
                contentScale = ContentScale.Fit
            )

            AppText(
                text = title,
                modifier = modifier.appPadding(SpacingToken.medium),
                textStyle = AppTypography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            AppText(
                text = description,
                textStyle = AppTypography.bodySmall,
            )
        }
    }
}