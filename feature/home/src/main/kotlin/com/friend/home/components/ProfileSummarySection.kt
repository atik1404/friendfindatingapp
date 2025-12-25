package com.friend.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.friend.common.extfun.getGreetingText
import com.friend.designsystem.spacing.IconSizeToken
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.theme.textColors
import com.friend.designsystem.typography.AppTypography
import com.friend.ui.components.AppIconButton
import com.friend.ui.components.AppText
import com.friend.ui.components.NetworkImageLoader
import com.friend.designsystem.R as Res

@Composable
fun ProfileSummarySection(
    fullName: String,
    profilePicture: String,
    navigateToChatListScreen: () -> Unit,
    navigateToProfileScreen: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        NetworkImageLoader(
            url = profilePicture,
            name = fullName,
            modifier = Modifier
                .size(IconSizeToken.extraLarge)
                .clickable {
                    navigateToProfileScreen.invoke()
                },
            shape = CircleShape,
        )

        Spacer(
            modifier = Modifier.width(SpacingToken.medium)
        )

        Column {
            AppText(
                text = stringResource(
                    Res.string.placeholder_greeting_message,
                    getGreetingText(), fullName
                ),
                textStyle = AppTypography.titleMedium,
                fontWeight = FontWeight.Bold,
                textColor = MaterialTheme.textColors.primary,
            )

            Spacer(
                modifier = Modifier.height(SpacingToken.micro)
            )

            AppText(
                text = stringResource(Res.string.msg_everything_set_for_you),
                textStyle = AppTypography.bodySmall,
                fontWeight = FontWeight.Light,
                textColor = MaterialTheme.textColors.primary,
            )
        }

        Spacer(
            modifier = Modifier.weight(1f)
        )

        AppIconButton(
            resourceIcon = Res.drawable.ic_chat_bubble,
            onClick = {
                navigateToChatListScreen.invoke()
            },
        )
    }
}