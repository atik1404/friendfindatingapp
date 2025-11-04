package com.friendfinapp.dating.navigation

import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import com.friend.chatlist.ChatListScreen

object ChatMessageNavGraph {
    fun register(
        backStack: NavBackStack,
        builder: EntryProviderBuilder<NavKey>,
    ) = with(builder) {
        entry(ChatMessageScreens.ChatListNavScreen) {
            ChatListScreen {
                backStack.removeLastOrNull()
            }
        }
    }
}