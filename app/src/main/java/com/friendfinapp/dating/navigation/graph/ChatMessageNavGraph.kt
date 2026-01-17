package com.friendfinapp.dating.navigation.graph

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.friend.chatlist.ChatListScreenRoute
import com.friend.chatroom.ConversationScreenRoute
import com.friend.forwardmessage.ForwardMessageScreenRoute
import com.friendfinapp.dating.navigation.ChatMessageScreens
import com.friendfinapp.dating.navigation.ProfileScreens

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
                        ChatMessageScreens.ConversationNavScreen(
                            chat = chat,
                        )
                    )
                }
            )
        }

        entry<ChatMessageScreens.ConversationNavScreen> { key ->
            ConversationScreenRoute(
                chat = key.chat,
                onBackButtonClicked = {
                    backStack.removeLastOrNull()
                },
                onNavigateToProfileScreen = { username ->
                    backStack.add(ProfileScreens.OtherProfileNavScreen(username = username))
                },
                onNavigateToReportScreen = { username ->
                    backStack.add(ProfileScreens.ReportUserNavScreen(username = username))
                },
                onNavigateToForwardMessageScreen = { messages ->
                    backStack.add(
                        ChatMessageScreens.ForwardMessageNavScreen(
                            messages = messages,
                        )
                    )
                }
            )
        }

        entry<ChatMessageScreens.ForwardMessageNavScreen> { key ->
            ForwardMessageScreenRoute(
                onBackButtonClicked = {
                    backStack.removeLastOrNull()
                },
                messages = key.messages,
            )
        }
    }
}