package com.friendfinapp.dating.navigation

import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import com.friend.chatlist.ChatListScreen
import com.friend.chatroom.ChatRoomScreen

object ChatMessageNavGraph {
    fun register(
        backStack: NavBackStack,
        builder: EntryProviderBuilder<NavKey>,
    ) = with(builder) {
        entry(ChatMessageScreens.ChatListNavScreen) {
            ChatListScreen(
                onBackButtonClicked = {
                    backStack.removeLastOrNull()
                },
                navigateToChatRoom = { username, messageId ->
                    backStack.add(
                        ChatMessageScreens.ChatRoomNavScreen(
                            userName = username,
                            messageId = messageId
                        )
                    )
                }
            )
        }

        entry<ChatMessageScreens.ChatRoomNavScreen> { key ->
            ChatRoomScreen(
                messageId = key.messageId,
                username = key.userName
            ) {
                backStack.removeLastOrNull()
            }
        }
    }
}