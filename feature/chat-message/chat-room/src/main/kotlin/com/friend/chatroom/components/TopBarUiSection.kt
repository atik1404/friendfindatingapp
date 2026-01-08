package com.friend.chatroom.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.friend.chatroom.bottomsheet.ReportUserBottomSheet
import com.friend.ui.components.PopupMenuType

@Composable
fun TopBarUiSection(
    fullName: String,
    userImage: String,
    modifier: Modifier,
    onBackButtonClicked: () -> Unit,
    onProfileImageClicked: () -> Unit,
    onSearchCanceled: () -> Unit,
    onReportAbuse: () -> Unit,
    onSearchApply: (String) -> Unit,
    isItemSelectionEnable: Boolean,
    onSelectionCancel: () -> Unit,
    onForwardMessage: () -> Unit,
    onDeleteMessage: () -> Unit,
) {
    var isSearchBarEnable by remember { mutableStateOf(false) }
    var showReportBottomSheet by rememberSaveable { mutableStateOf(false) }

    if (isSearchBarEnable && !isItemSelectionEnable)
        SearchBarUi(
            modifier = modifier,
            onCancelClicked = {
                isSearchBarEnable = false
                onSearchCanceled.invoke()
            },
            onSearchApply = {
                onSearchApply.invoke(it)
            }
        )
    else ProfileInfoHeader(
        fullName = fullName,
        userImage = userImage,
        modifier = modifier,
        backToChatListScreen = onBackButtonClicked,
        onMenuClicked = {
            when (it) {
                PopupMenuType.ReportAbuse -> showReportBottomSheet = true
                PopupMenuType.MessageSearch -> isSearchBarEnable = true
                else -> {}
            }
        },
        onProfileImageClicked = onProfileImageClicked,
        isItemSelectionEnable = isItemSelectionEnable,
        onSelectionCancel = onSelectionCancel,
        onForwardMessage = onForwardMessage,
        onDeleteMessage = onDeleteMessage
    )

    if (showReportBottomSheet)
        ReportUserBottomSheet(
            onReportUser = {
                onReportAbuse.invoke()
                showReportBottomSheet = false
            },
            onDismissRequest = {
                showReportBottomSheet = false
            },
        )
}