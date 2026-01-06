package com.friend.chatroom.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.friend.chatroom.bottomsheet.MessageForwardBottomSheet
import com.friend.chatroom.bottomsheet.ReportUserBottomSheet
import com.friend.ui.components.PopupMenuType

@Composable
fun TopBarUiSection(
    userName: String,
    userImage: String,
    modifier: Modifier,
    onBackButtonClicked: () -> Unit,
    onProfileImageClicked: () -> Unit,
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
        userImage = userImage,
        modifier = modifier,
        backToChatListScreen = onBackButtonClicked,
        onMenuClicked = {
            when (it) {
                PopupMenuType.ReportAbuse -> showReportBottomSheet = true
                PopupMenuType.MessageSearch -> isSearchBarEnable = true
                PopupMenuType.ForwardMessage -> showForwardBottomSheet = true
                else -> {}
            }
        },
        onProfileImageClicked = onProfileImageClicked
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