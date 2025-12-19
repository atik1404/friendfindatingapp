package com.friend.otherprofile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.friend.designsystem.spacing.ImageSizeToken
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPaddingOnly
import com.friend.designsystem.theme.backgroundColors
import com.friend.designsystem.theme.textColors
import com.friend.designsystem.typography.AppTypography
import com.friend.ui.components.AppIconButton
import com.friend.ui.components.AppText
import com.friend.ui.components.NetworkImageLoader
import com.friend.ui.preview.LightDarkPreview
import com.friend.designsystem.R as Res

@Composable
fun ProfileHeaderUi(
    modifier: Modifier = Modifier,
    fullName: String,
    email: String,
    profilePicture: String,
    onSendMsgClicked: () -> Unit,
) {
    val picSize = ImageSizeToken.profilePictureLarge

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.backgroundColors.white,  // background
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer    // text/icon color
        )
    ) {
        Row(
            modifier = modifier
                .appPaddingOnly(
                    start = SpacingToken.extraSmall,
                    top = SpacingToken.medium,
                    bottom = SpacingToken.medium
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            NetworkImageLoader(
                url = profilePicture,
                shape = CircleShape,
                modifier = Modifier.size(picSize)
            )

            Spacer(modifier = modifier.width(SpacingToken.medium))

            Column(
                modifier = modifier.weight(1f)
            ) {
                AppText(
                    text = fullName,
                    textStyle = AppTypography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    textColor = MaterialTheme.textColors.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(
                    modifier = Modifier.height(SpacingToken.micro)
                )

                AppText(
                    text = email,
                    textStyle = AppTypography.bodyMedium,
                    fontWeight = FontWeight.Light,
                    textColor = MaterialTheme.textColors.primary,
                    maxLines = 2
                )
            }

            AppIconButton(
                resourceIcon = Res.drawable.ic_chat_bubble,
                onClick = {
                    onSendMsgClicked.invoke()
                },
            )
        }
    }
}

@Composable
@LightDarkPreview
private fun ScreenPreview() {
    ProfileHeaderUi(
        fullName = "Tom Cruise",
        email = "tom@gmail.com",
        profilePicture = "https://images.mubicdn.net/images/cast_member/2184/cache-2992-1547409411/image-w856.jpg",
        onSendMsgClicked = {},
    )
}
