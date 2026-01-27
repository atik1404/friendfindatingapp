package com.friendfinapp.dating.navigation.graph

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.friend.chatlist.ChatListScreenRoute
import com.friend.chatroom.ConversationScreenRoute
import com.friend.forwardmessage.ForwardMessageScreenRoute
import com.friendfinapp.dating.navigation.ChatMessageScreens
import com.friendfinapp.dating.navigation.CommonScreens
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
                            toUsername = chat.toUsername,
                            fullName = chat.fullName,
                            imageUrl = chat.userImage
                        )
                    )
                }
            )
        }

        entry<ChatMessageScreens.ConversationNavScreen> { key ->
            ConversationScreenRoute(
                toUsername = key.toUsername,
                fullName = key.fullName,
                imageUrl = key.imageUrl,
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
                },
                onNavigateToPlayerScreen = { url ->
                    backStack.add(
                        CommonScreens.VideoPlayerNavScreen(
                            url
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
                navigateToConversationScreen = { toUsername, imageUrl, fullName ->
                    backStack.removeLastOrNull()
                    backStack.removeLastOrNull()
                    backStack.add(
                        ChatMessageScreens.ConversationNavScreen(
                            toUsername = toUsername,
                            fullName = fullName,
                            imageUrl = imageUrl
                        )
                    )
                },
                messages = key.messages,
            )
        }
    }
}