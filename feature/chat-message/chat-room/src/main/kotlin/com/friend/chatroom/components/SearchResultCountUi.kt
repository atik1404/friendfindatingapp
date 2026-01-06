package com.friend.chatroom.components

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.friend.designsystem.spacing.RadiusToken
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPaddingOnly
import com.friend.designsystem.theme.surfaceColors
import com.friend.designsystem.typography.AppTypography
import com.friend.ui.components.AppIconButton
import com.friend.ui.components.AppText
import com.friend.ui.preview.LightPreview
import com.friend.designsystem.R as Res

@Composable
fun SearchResultCountUi(
    modifier: Modifier = Modifier,
    count: Int,
    keyword: String,
    onClick: () -> Unit
) {
    AppText(
        modifier = modifier
            .background(
                color = MaterialTheme.surfaceColors.greenBase.copy(alpha = .3f),
                shape = RoundedCornerShape(RadiusToken.large)
            )
            .appPaddingOnly(start = SpacingToken.extraSmall),
        text = stringResource(Res.string.placeholder_message_search_result_count, count, keyword),
        trailing = {
            AppIconButton(
                vectorIcon = Icons.Rounded.Clear,
                onClick = onClick
            )
        },
        fontWeight = FontWeight.Light,
        textStyle = MaterialTheme.typography.bodySmall
    )
}

@Composable
@LightPreview
private fun ScreenPreview() {
    SearchResultCountUi(
        count = 10,
        keyword = "Tom Cruise",
        onClick = {}
    )
}