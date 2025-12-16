package com.friendfinapp.dating.navigation.graph

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.friend.chatlist.ChatListScreenRoute
import com.friend.chatroom.ChatRoomScreen
import com.friendfinapp.dating.navigation.ChatMessageScreens

object ChatMessageNavGraph {
    fun register(
        backStack: NavBackStack<NavKey>,
        builder: EntryProviderScope<NavKey>
    ) = with(builder) {
        entry(ChatMessageScreens.ChatListNavScreen) {
            ChatListScreenRoute(
                onBackButtonClicked = {
                    backStack.removeLastOrNull()
                },
                navigateToChatRoom = { chat ->
                    backStack.add(
                        ChatMessageScreens.ChatRoomNavScreen(
                            userName = chat.toUsername,
                            messageId = chat.toUsername
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