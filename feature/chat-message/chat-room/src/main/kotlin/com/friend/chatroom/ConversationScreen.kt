package com.friend.chatroom

import AppDivider
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.friend.chatroom.components.AnimatedAttachmentType
import com.friend.chatroom.components.AttachedFilePreviewUi
import com.friend.chatroom.components.ConversionsUiSection
import com.friend.chatroom.components.SearchResultCountUi
import com.friend.chatroom.components.TopBarUiSection
import com.friend.chatroom.components.UserInputForm
import com.friend.chatroom.components.conversation.JumpToBottom
import com.friend.common.utils.FilesUtils
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPaddingOnly
import com.friend.designsystem.theme.dividerColors
import com.friend.ui.common.LoadingUi
import com.friend.ui.components.AppScaffold
import com.friend.ui.preview.LightPreview
import kotlinx.coroutines.launch
import timber.log.Timber

private val JumpToBottomThreshold = 56.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationScreen(
    modifier: Modifier = Modifier,
    uiState: UiState,
    listState: LazyListState,
    toUsername: String,
    fullName: String,
    imageUrl: String,
    onBackButtonClicked: () -> Unit,
    onNavigateToProfileScreen: () -> Unit,
    onNavigateToReportScreen: () -> Unit,
    onNavigateToForwardMessageScreen: () -> Unit,
    onNavigateToPlayerScreen: (String) -> Unit,
    onAction: (UiAction) -> Unit,
) {
    var isItemSelectionEnable by remember { mutableStateOf(false) }
    var isAttachmentExpanded by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    val jumpThreshold = with(LocalDensity.current) {
        JumpToBottomThreshold.toPx()
    }

    val jumpToBottomButtonEnabled by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex != 0 ||
                    listState.firstVisibleItemScrollOffset > jumpThreshold
        }
    }

    AppScaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        isAdsVisible = false
    ) { padding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .consumeWindowInsets(padding)
                .navigationBarsPadding()
                .imePadding()
        ) {
            Column(
                modifier = modifier
                    .fillMaxSize()
            ) {
                TopBarUiSection(
                    modifier = modifier
                        .statusBarsPadding()
                        .navigationBarsPadding()
                        .imePadding(),
                    fullName = fullName,
                    userImage = imageUrl,
                    onProfileImageClicked = onNavigateToProfileScreen,
                    onBackButtonClicked = onBackButtonClicked,
                    onSearchCanceled = {
                        onAction.invoke(UiAction.OnClearSearch)
                        onAction.invoke(UiAction.FetchMessages(toUsername))
                    },
                    onSearchApply = {
                        onAction.invoke(
                            UiAction.SearchMessage(
                                toUsername,
                                it
                            )
                        )
                    },
                    onReportAbuse = onNavigateToReportScreen,
                    isItemSelectionEnable = isItemSelectionEnable,
                    onSelectionCancel = {
                        isItemSelectionEnable = false
                        onAction.invoke(UiAction.OnClearMessageSelection)
                    },
                    onForwardMessage = {
                        if (uiState.isAnyItemSelected) {
                            isItemSelectionEnable = false
                            onNavigateToForwardMessageScreen.invoke()
                        }
                    },
                    onDeleteMessage = {
                        if (uiState.isAnyItemSelected) {
                            isItemSelectionEnable = false
                            onAction.invoke(UiAction.DeleteMessages)
                        }
                    }
                )

                AppDivider(
                    color = MaterialTheme.dividerColors.primary
                )

                ConversionsUiSection(
                    listState = listState,
                    message = uiState.conversations,
                    modifier = modifier
                        .fillMaxWidth()
                        .weight(1f),
                    isItemSelectionEnable = isItemSelectionEnable,
                    onLongPress = {
                        isItemSelectionEnable = true
                    },
                    onItemSelected = {
                        onAction.invoke(UiAction.UpdateMessageSelectionStatus(it))
                    },
                    onNavigateToPlayerScreen = onNavigateToPlayerScreen
                )

                AnimatedAttachmentType(
                    isAttachmentExpanded,
                    modifier = modifier,
                    pickImage = {
                        isAttachmentExpanded = false
                        onAction.invoke(UiAction.OnChangeImageAttachment(it))
                    },
                    pickVideo = {
                        Timber.e("videoDuration: ${FilesUtils.getFileDurationMs(it)}")
                        isAttachmentExpanded = false
                        onAction.invoke(UiAction.OnChangeVideoAttachment(it))
                        onAction.invoke(UiAction.SendMessage(toUsername))
                    },
                )

                if (uiState.messageContent.image !== null) {
                    Spacer(modifier = modifier.height(SpacingToken.medium))

                    val bitmap = FilesUtils.fileToBitmap(
                        uiState.messageContent.image
                    )

                    AttachedFilePreviewUi(
                        bitmap = bitmap,
                        modifier = modifier,
                        clearFile = {
                            onAction.invoke(UiAction.OnChangeImageAttachment(null))
                        }
                    )
                    Spacer(modifier = modifier.height(SpacingToken.medium))
                }

                UserInputForm(
                    modifier = Modifier
                        .appPaddingOnly(bottom = SpacingToken.medium),
                    isSendEnable = uiState.messageContent.isSendEnable,
                    textMessage = uiState.messageContent.textMessage,
                    onTextChange = {
                        onAction.invoke(UiAction.OnChangeTextMessage(it))
                    },
                    onClickAttachment = {
                        isAttachmentExpanded = !isAttachmentExpanded
                    },
                    onCaptureImage = {
                        onAction.invoke(UiAction.OnChangeImageAttachment(it))
                    },
                    onSendTextMessage = {
                        onAction.invoke(UiAction.SendMessage(toUsername))
                    }
                )
            }

            if (uiState.isLoading)
                LoadingUi()

            if (uiState.isSearchEnabled)
                Column(
                    modifier = modifier
                        .align(alignment = Alignment.TopCenter)
                        .appPaddingOnly(top = SpacingToken.hugePlusPlusPlus),
                ) {
                    SearchResultCountUi(
                        count = uiState.conversations.size,
                        keyword = uiState.searchKey
                    )
                }

            JumpToBottom(
                // Only show if the scroller is not at the bottom
                enabled = jumpToBottomButtonEnabled,
                onClicked = {
                    scope.launch {
                        listState.animateScrollToItem(0)
                    }
                },
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }
    }
}

@Composable
@LightPreview
private fun ScreenPreview() {
    ConversationScreen(
        uiState = UiState(),
        listState = rememberLazyListState(),
        toUsername = "",
        fullName = "",
        imageUrl = "",
        onBackButtonClicked = {},
        onNavigateToProfileScreen = {},
        onAction = {},
        onNavigateToReportScreen = {},
        onNavigateToForwardMessageScreen = {},
        onNavigateToPlayerScreen = {}
    )
}