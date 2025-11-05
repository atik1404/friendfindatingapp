package com.friend.chatroom

import AppDivider
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.friend.designsystem.spacing.IconSizeToken
import com.friend.designsystem.spacing.RadiusToken
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPadding
import com.friend.designsystem.spacing.appPaddingHorizontal
import com.friend.designsystem.spacing.appPaddingOnly
import com.friend.designsystem.spacing.appPaddingVertical
import com.friend.designsystem.theme.backgroundColors
import com.friend.designsystem.theme.buttonColors
import com.friend.designsystem.theme.dividerColors
import com.friend.designsystem.theme.surfaceColors
import com.friend.designsystem.theme.textColors
import com.friend.designsystem.theme.textFieldColors
import com.friend.designsystem.typography.AppTypography
import com.friend.ui.components.AppBaseTextField
import com.friend.ui.components.AppIconButton
import com.friend.ui.components.AppPopupMenu
import com.friend.ui.components.AppScaffold
import com.friend.ui.components.AppText
import com.friend.ui.components.NetworkImageLoader
import com.friend.ui.preview.LightDarkPreview
import timber.log.Timber
import com.friend.designsystem.R as Res

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatRoomScreen(
    username: String,
    messageId: String,
    onBackButtonClicked: () -> Unit,
) {
    var isExpanded by remember { mutableStateOf(false) }

    AppScaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
    ) { padding ->
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .consumeWindowInsets(padding)
                .navigationBarsPadding()
                .imePadding()
        ) {
            val (profileInfo, divider, messageList, messageSendUi) = createRefs()

            ProfileInfo(
                username = "$username - $messageId",
                modifier = Modifier.constrainAs(profileInfo) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            ) {
                onBackButtonClicked.invoke()
            }
            AppDivider(
                modifier = Modifier.constrainAs(divider) {
                    top.linkTo(profileInfo.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                color = MaterialTheme.dividerColors.primary
            )
            MessageList(
                modifier = Modifier
                    .constrainAs(messageList) {
                        top.linkTo(divider.bottom)
                        bottom.linkTo(messageSendUi.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
                    .appPaddingVertical(SpacingToken.extraSmall)
            )

            Column(
                modifier = Modifier
                    .constrainAs(messageSendUi) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ) {
                if(isExpanded)
                    AttachmentTypeUi()

                MessageSendUi(
                    modifier = Modifier
                        .appPaddingOnly(bottom = SpacingToken.medium),
                    onClickAttachment = {
                        isExpanded = !isExpanded
                    }
                )
            }
        }
    }
}

@Composable
private fun ProfileInfo(
    modifier: Modifier,
    username: String,
    navigateToChatListScreen: () -> Unit,
) {
    Row(
        modifier = modifier
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

        AppPopupMenu(
            icon = Icons.Default.MoreVert,
            menuItems = listOf("Report User", "Message Search",),
            onClick = {
                Timber.e("Clicked: $it")
            }
        )
    }
}

@Composable
private fun MessageItem(isMyMessage: Boolean) {
    val backgroundColor = bubbleColors(isMyMessage).first
    val contentColor = bubbleColors(isMyMessage).second
    val bubbleShape = bubbleShape(isMyMessage)
    val alignment = if (isMyMessage) Alignment.End else Alignment.Start

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .appPaddingOnly(top = SpacingToken.medium),
        horizontalAlignment = alignment
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
                    color = backgroundColor,
                    shape = bubbleShape
                )
                .appPadding(SpacingToken.tiny),
            horizontalAlignment = alignment
        ) {
            AppText(
                text = "Hey, How are you Atik?Hey, How are you Atik?",
                textStyle = AppTypography.bodyMedium,
                textColor = contentColor,
                maxLines = 50
            )

            Spacer(
                modifier = Modifier.height(SpacingToken.micro)
            )

            AppText(
                text = "04:18 AM",
                textStyle = AppTypography.bodySmall,
                fontWeight = FontWeight.Light,
                textColor = contentColor,
            )
        }
    }
}

@Composable
private fun MessageList(
    modifier: Modifier
) {
    LazyColumn(
        reverseLayout = true,
        modifier = modifier
            .appPaddingHorizontal(SpacingToken.small)
    ) {
        items(100) { index ->
            MessageItem(index % 2 == 0)
        }
    }
}

@Composable
private fun MessageSendUi(
    modifier: Modifier,
    onClickAttachment: () -> Unit
) {
    var message by remember { mutableStateOf("") }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .appPaddingHorizontal(SpacingToken.tiny),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .height(IntrinsicSize.Min)
                .background(
                    color = MaterialTheme.backgroundColors.white,
                    shape = RoundedCornerShape(SpacingToken.extraLarge)
                )
                .appPaddingHorizontal(SpacingToken.tiny),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppBaseTextField(
                singleLine = false,
                maxLines = 3,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                shape = RoundedCornerShape(SpacingToken.extraLarge),
                onValueChange = { message = it },
                value = message,
                placeholder = stringResource(Res.string.hint_write_something_here),
                colors = MaterialTheme.textFieldColors.transparentOutlinedTextField
            )

            AppIconButton(
                modifier = Modifier.size(IconSizeToken.large),
                onClick = onClickAttachment,
                vectorIcon = Icons.Default.AttachFile
            )

            Spacer(Modifier.width(SpacingToken.tiny))

            AppIconButton(
                modifier = Modifier.size(IconSizeToken.large),
                onClick = {},
                vectorIcon = Icons.Default.CameraAlt
            )
        }

        Spacer(Modifier.width(SpacingToken.tiny))

        if (message.isEmpty()) {
            AppIconButton(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.buttonColors.primaryButton.disabledContainerColor,
                        shape = CircleShape
                    )
                    .size(IconSizeToken.mediumLarge),
                onClick = {},
                vectorIcon = Icons.Default.Mic
            )
        } else {
            AppIconButton(
                modifier = Modifier
                    .size(IconSizeToken.mediumLarge),
                onClick = {},
                vectorIcon = Icons.Default.Send
            )
        }
    }
}

/** --- Bubble shape & color helpers --- */
@Composable
private fun bubbleShape(isMe: Boolean): Shape {
    return if (isMe) {
        RoundedCornerShape(
            topEnd = RadiusToken.medium,
            topStart = RadiusToken.xxl,
            bottomEnd = RadiusToken.medium,
        )
    } else {
        RoundedCornerShape(
            topEnd = RadiusToken.xxl,
            topStart = RadiusToken.medium,
            bottomStart = RadiusToken.medium,
        )
    }
}

@Composable
private fun bubbleColors(isMe: Boolean): Pair<Color, Color> {
    return if (isMe) {
        // sender (me)
        MaterialTheme.surfaceColors.primary.copy(alpha = .7f) to MaterialTheme.textColors.white
    } else {
        // receiver
        MaterialTheme.surfaceColors.yellowLight.copy(alpha = .5f) to MaterialTheme.textColors.primary
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