package com.friend.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.friend.designsystem.spacing.IconSizeToken
import com.friend.designsystem.theme.textColors
import com.friend.designsystem.typography.AppTypography
import com.friend.ui.components.AppIconButton
import com.friend.ui.components.AppText
import com.friend.ui.components.ResourceImageLoader
import com.friend.designsystem.R as Res

@Composable
fun SearchBarSection(
    navigateToOverviewScreen: () -> Unit,
    showBottomSheet: () -> Unit,
    navigateToMembership: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AppIconButton(
            vectorIcon = Icons.Default.Dashboard,
            onClick = navigateToOverviewScreen
        )
        Spacer(modifier = Modifier.weight(1f))
        AppText(
            text = stringResource(Res.string.title_friend_fin),
            fontWeight = FontWeight.Bold,
            textStyle = AppTypography.titleLarge,
            textColor = MaterialTheme.textColors.brand.copy(alpha = .7f),
            trailing = {
                ResourceImageLoader(
                    imageResId = Res.drawable.img_vip,
                    modifier = Modifier.size(IconSizeToken.medium),
                )
            },
            onClick = navigateToMembership
        )
        Spacer(modifier = Modifier.weight(1f))
        AppIconButton(
            resourceIcon = Res.drawable.ic_filter,
            onClick = showBottomSheet
        )
    }
}