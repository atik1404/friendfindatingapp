package com.friend.overview.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.friend.designsystem.spacing.IconSizeToken
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPaddingSymmetric
import com.friend.designsystem.theme.backgroundColors
import com.friend.designsystem.theme.textColors
import com.friend.designsystem.typography.AppTypography
import com.friend.ui.components.AppIconButton
import com.friend.ui.components.AppText
import com.friend.ui.components.NetworkImageLoader

@Composable
fun ProfileSummaryUi(
    modifier: Modifier
){
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.backgroundColors.white,  // background
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer    // text/icon color
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .appPaddingSymmetric(
                    horizontal = SpacingToken.medium,
                    vertical = SpacingToken.large
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            NetworkImageLoader(
                "https://images.unsplash.com/photo-1483909796554-bb0051ab60ad?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&q=80&w=2373",
                modifier = Modifier
                    .size(IconSizeToken.extraLarge)
                    .clickable{

                    },
                shape = CircleShape
            )

            Spacer(
                modifier = Modifier.width(SpacingToken.medium)
            )

            Column {
                AppText(
                    text = "Atik Faysal",
                    textStyle = AppTypography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    textColor = MaterialTheme.textColors.primary,
                )

                Spacer(
                    modifier = Modifier.height(SpacingToken.micro)
                )

                AppText(
                    text = "atik@gmail.com",
                    textStyle = AppTypography.bodyMedium,
                    fontWeight = FontWeight.Light,
                    textColor = MaterialTheme.textColors.primary,
                )
            }

            Spacer(
                modifier = Modifier.weight(1f)
            )

            AppIconButton(
                onClick = {

                },
                vectorIcon = Icons.Default.KeyboardArrowRight
            )
        }
    }
}