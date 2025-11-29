package com.friend.login.components

import AppDivider
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.friend.designsystem.R as Res
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPaddingHorizontal
import com.friend.designsystem.spacing.appPaddingOnly
import com.friend.designsystem.theme.dividerColors
import com.friend.designsystem.theme.textColors
import com.friend.ui.components.AppText

@Composable
fun LoginOptionDivider(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .appPaddingOnly(
                top = SpacingToken.medium,
                start = SpacingToken.large,
                end = SpacingToken.large
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AppDivider(
            modifier = Modifier.weight(1f),
            color = MaterialTheme.dividerColors.primary
        )

        AppText(
            text = stringResource(Res.string.label_or),
            fontWeight = FontWeight.Medium,
            textColor = MaterialTheme.textColors.primary,
            modifier = Modifier.appPaddingHorizontal(horizontal = SpacingToken.small)
        )

        AppDivider(
            modifier = Modifier.weight(1f),
            color = MaterialTheme.dividerColors.primary
        )
    }
}