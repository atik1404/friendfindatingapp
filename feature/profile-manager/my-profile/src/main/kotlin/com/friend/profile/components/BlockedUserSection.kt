package com.friend.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.friend.designsystem.spacing.IconSizeToken
import com.friend.designsystem.spacing.RadiusToken
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPadding
import com.friend.designsystem.spacing.appPaddingVertical
import com.friend.designsystem.theme.backgroundColors
import com.friend.designsystem.theme.textColors
import com.friend.designsystem.typography.AppTypography
import com.friend.entity.profilemanager.BlockedUserEntity
import com.friend.ui.components.AppText
import com.friend.ui.components.AppTextButton
import com.friend.ui.components.NetworkImageLoader
import com.friend.ui.preview.LightPreview
import com.friend.designsystem.R as Res

@Composable
fun BlockedUserSection(
    modifier: Modifier = Modifier,
    blockers: List<BlockedUserEntity> = emptyList(),
    onUnblock: (String) -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.backgroundColors.white,
                shape = RoundedCornerShape(RadiusToken.medium)
            )
            .appPadding(SpacingToken.medium)
    ) {
        AppText(
            text = stringResource(Res.string.label_blocked_list),
            fontWeight = FontWeight.Light,
            textStyle = AppTypography.bodySmall
        )

        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .heightIn(max = 300.dp)
        ) {
            items(
                items = blockers,
                key = { it.fullName }
            ) {
                BlockedUserItem(user = it) {
                    onUnblock(it.username)
                }
            }
        }
    }
}

@Composable
private fun BlockedUserItem(
    modifier: Modifier = Modifier,
    user: BlockedUserEntity,
    onUnblock: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .appPaddingVertical(SpacingToken.micro),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        NetworkImageLoader(
            url = "https://images.mubicdn.net/images/cast_member/2184/cache-2992-1547409411/image-w856.jpg",
            name = "ABCD",
            shape = CircleShape,
            modifier = modifier.size(IconSizeToken.large)
        )

        Spacer(modifier = modifier.width(SpacingToken.extraSmall))

        AppText(
            modifier = modifier.weight(1f),
            text = user.fullName,
            textStyle = AppTypography.titleMedium,
            textColor = MaterialTheme.textColors.primary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = modifier.width(SpacingToken.extraSmall))

        AppTextButton(
            text = stringResource(Res.string.action_unblock),
            onClick = onUnblock,
            textColor = MaterialTheme.textColors.brand,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
@LightPreview
private fun ScreenPreview() {
    BlockedUserSection(
        blockers = listOf(
            BlockedUserEntity(
                username = "Tom Cruise",
                fullName = "Tom Cruise",
                userImage = "",
            ), BlockedUserEntity(
                username = "Tom Cruise1",
                fullName = "Tom Cruise2",
                userImage = "",
            )
        )
    )
}