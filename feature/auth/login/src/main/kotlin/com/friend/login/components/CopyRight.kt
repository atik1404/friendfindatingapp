package com.friend.login.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.friend.designsystem.R as Res
import com.friend.designsystem.theme.textColors
import com.friend.designsystem.typography.AppTypography
import com.friend.ui.components.AppText

@Composable
fun CopyrightText(
    modifier: Modifier = Modifier
) {
    AppText(
        modifier = modifier,
        text = stringResource(Res.string.msg_copyright),
        fontWeight = FontWeight.Light,
        textStyle = AppTypography.bodySmall,
        textColor = MaterialTheme.textColors.secondary,
    )
}