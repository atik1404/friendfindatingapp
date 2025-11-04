package com.friend.chatroom

import AppDivider
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.friend.designsystem.spacing.IconSizeToken
import com.friend.designsystem.spacing.RadiusToken
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPadding
import com.friend.designsystem.spacing.appPaddingHorizontal
import com.friend.designsystem.spacing.appPaddingOnly
import com.friend.designsystem.theme.backgroundColors
import com.friend.designsystem.theme.dividerColors
import com.friend.designsystem.theme.textColors
import com.friend.designsystem.typography.AppTypography
import com.friend.ui.components.AppIconButton
import com.friend.ui.components.AppScaffold
import com.friend.ui.components.AppText
import com.friend.ui.components.NetworkImageLoader
import com.friend.ui.preview.LightDarkPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatRoomScreen(
    username: String,
    messageId: String,
    onBackButtonClicked: () -> Unit,
) {
    AppScaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .consumeWindowInsets(padding)
                .navigationBarsPadding()
                .imePadding()
        ) {
            ProfileInfo(username) {
                onBackButtonClicked.invoke()
            }
            AppDivider(color = MaterialTheme.dividerColors.primary)
            Spacer(Modifier.height(SpacingToken.medium))
            MessageList()
        }
    }
}


@Composable
private fun ProfileInfo(
    username: String,
    navigateToChatListScreen: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.backgroundColors.white)
            .appPadding(SpacingToken.extraSmall),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = {
                navigateToChatListScreen.invoke()
            }
        ) {
            Icon(
                contentDescription = "",
                imageVector = Icons.Default.ArrowBackIosNew,
            )
        }

        NetworkImageLoader(
            "https://images.unsplash.com/photo-1483909796554-bb0051ab60ad?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&q=80&w=2373",
            modifier = Modifier
                .size(IconSizeToken.extraLarge),
            shape = CircleShape
        )

        Spacer(
            modifier = Modifier.width(SpacingToken.medium)
        )

        AppText(
            text = username,
            textStyle = AppTypography.titleMedium,
            fontWeight = FontWeight.Bold,
            textColor = MaterialTheme.textColors.primary,
        )

        Spacer(
            modifier = Modifier.weight(1f)
        )

        AppIconButton(
            onClick = {

            },
            vectorIcon = Icons.Default.MoreVert
        )
    }
}

@Composable
private fun ReceiverMessageItem() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .appPaddingOnly(top = SpacingToken.medium),
    ) {
        AppText(
            text = "September 23, 2025",
            textStyle = AppTypography.bodySmall,
            fontWeight = FontWeight.Medium,
            textColor = MaterialTheme.textColors.primary,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(
            modifier = Modifier.height(SpacingToken.medium)
        )

        AppText(
            text = "Atik Faysal",
            textStyle = AppTypography.bodySmall,
            fontWeight = FontWeight.Light,
            textColor = MaterialTheme.textColors.primary,
        )

        Spacer(
            modifier = Modifier.height(SpacingToken.micro)
        )

        Column(
            modifier = Modifier
                .background(
                    Color.Yellow.copy(alpha = .5f),
                    RoundedCornerShape(
                        topEnd = RadiusToken.xxl,
                        topStart = RadiusToken.medium,
                        bottomStart = RadiusToken.medium,
                    )
                )
                .appPadding(SpacingToken.tiny)
        ) {
            AppText(
                text = "Hey, How are you Atik?Hey, How are you Atik?",
                textStyle = AppTypography.bodyMedium,
                textColor = MaterialTheme.textColors.primary,
                maxLines = 50
            )

            Spacer(
                modifier = Modifier.height(SpacingToken.micro)
            )

            AppText(
                text = "04:18 AM",
                textStyle = AppTypography.bodySmall,
                fontWeight = FontWeight.Light,
                textColor = MaterialTheme.textColors.primary,
            )
        }
    }
}

@Composable
private fun SenderMessageItem() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .appPaddingOnly(top = SpacingToken.medium),
        horizontalAlignment = Alignment.End
    ) {
        AppText(
            text = "September 23, 2025",
            textStyle = AppTypography.bodySmall,
            fontWeight = FontWeight.Medium,
            textColor = MaterialTheme.textColors.primary,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(
            modifier = Modifier.height(SpacingToken.medium)
        )

        AppText(
            text = "Atik Faysal",
            textStyle = AppTypography.bodySmall,
            fontWeight = FontWeight.Light,
            textColor = MaterialTheme.textColors.primary,
        )

        Spacer(
            modifier = Modifier.height(SpacingToken.micro)
        )

        Column(
            modifier = Modifier
                .background(
                    Color.Blue.copy(alpha = .5f),
                    RoundedCornerShape(
                        topEnd = RadiusToken.medium,
                        topStart = RadiusToken.xxl,
                        bottomEnd = RadiusToken.medium,
                    )
                )
                .appPadding(SpacingToken.tiny)
        ) {
            AppText(
                text = "Hey, How are you Atik?Hey, How are you Atik?",
                textStyle = AppTypography.bodyMedium,
                textColor = MaterialTheme.textColors.white,
                maxLines = 50
            )

            Spacer(
                modifier = Modifier.height(SpacingToken.micro)
            )

            AppText(
                text = "04:18 AM",
                textStyle = AppTypography.bodySmall,
                fontWeight = FontWeight.Light,
                textColor = MaterialTheme.textColors.white,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

@Composable
private fun MessageList() {
    LazyColumn(
        reverseLayout = true,
        modifier = Modifier.appPaddingHorizontal(SpacingToken.small)
    ) {
        items(100) { index ->
            if (index % 2 == 0)
                ReceiverMessageItem()
            else SenderMessageItem()
        }
    }
}

@Composable
@LightDarkPreview
private fun ScreenPreview() {
    ChatRoomScreen(
        username = "Atik Faysal",
        messageId = "123456789",
    ) {

    }
}