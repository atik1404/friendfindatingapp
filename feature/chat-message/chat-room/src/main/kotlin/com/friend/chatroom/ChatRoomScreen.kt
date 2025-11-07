package com.friend.chatroom

import AppDivider
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.friend.chatroom.bottomsheet.MessageForwardBottomSheet
import com.friend.chatroom.bottomsheet.ReportUserBottomSheet
import com.friend.chatroom.ui.AttachmentTypeUi
import com.friend.chatroom.ui.MessageItem
import com.friend.chatroom.ui.MessageSendUi
import com.friend.chatroom.ui.ProfileInfoHeader
import com.friend.chatroom.ui.SearchBarUi
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPaddingHorizontal
import com.friend.designsystem.spacing.appPaddingOnly
import com.friend.designsystem.spacing.appPaddingVertical
import com.friend.designsystem.theme.dividerColors
import com.friend.ui.components.AppScaffold
import com.friend.ui.components.PopupMenuType
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
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .consumeWindowInsets(padding)
                .navigationBarsPadding()
                .imePadding()
        ) {
            val (topbar, divider, messageList, messageSendUi) = createRefs()

            TopbarUi(
                userName = "$username - $messageId",
                modifier = Modifier.constrainAs(topbar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            ) {
                onBackButtonClicked.invoke()
            }
            AppDivider(
                modifier = Modifier.constrainAs(divider) {
                    top.linkTo(topbar.bottom)
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

            BottomFormUi(
                modifier = Modifier
                    .constrainAs(messageSendUi) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )
        }
    }
}

@Composable
private fun TopbarUi(
    userName: String,
    modifier: Modifier,
    onBackButtonClicked: () -> Unit
) {
    var isSearchBarEnable by remember { mutableStateOf(false) }
    var showReportBottomSheet by rememberSaveable { mutableStateOf(false) }
    var showForwardBottomSheet by rememberSaveable { mutableStateOf(false) }

    if (isSearchBarEnable)
        SearchBarUi(
            modifier = modifier,
            onCancelClicked = {
                isSearchBarEnable = false
            }
        )
    else ProfileInfoHeader(
        username = userName,
        modifier = modifier,
        backToChatListScreen = onBackButtonClicked,
        onMenuClicked = {
            when (it) {
                PopupMenuType.ReportUser -> showReportBottomSheet = true
                PopupMenuType.MessageSearch -> isSearchBarEnable = true
                PopupMenuType.ForwardMessage -> showForwardBottomSheet = true
                else -> {}
            }
        }
    )


    if (showForwardBottomSheet)
        MessageForwardBottomSheet(
            modifier = modifier,
            onForward = {
                showForwardBottomSheet = false
            },
            onDismiss = {
                showForwardBottomSheet = false
            }
        )

    if (showReportBottomSheet)
        ReportUserBottomSheet(
            onReportUser = {
                showReportBottomSheet = false
            },
            onDismissRequest = {
                showReportBottomSheet = false
            },
        )
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
            MessageItem(
                modifier = Modifier,
                index % 2 == 0
            )
        }
    }
}

@Composable
private fun BottomFormUi(
    modifier: Modifier
) {
    var isAttachmentExpanded by remember { mutableStateOf(false) }
    Column(
        modifier = modifier
    ) {
        if (isAttachmentExpanded)
            AttachmentTypeUi()

        MessageSendUi(
            modifier = Modifier
                .appPaddingOnly(bottom = SpacingToken.medium),
            onClickAttachment = {
                isAttachmentExpanded = !isAttachmentExpanded
            }
        )
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