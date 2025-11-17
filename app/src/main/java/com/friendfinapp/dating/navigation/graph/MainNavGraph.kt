package com.friendfinapp.dating.navigation.graph

import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import com.friend.home.HomeScreen
import com.friend.membership.MembershipScreen
import com.friend.policy.PrivacyPolicyScreen
import com.friendfinapp.dating.navigation.ChatMessageScreens
import com.friendfinapp.dating.navigation.MainScreens
import com.friendfinapp.dating.navigation.ProfileScreens

object MainNavGraph {
    fun register(
        backStack: NavBackStack,
        builder: EntryProviderBuilder<NavKey>
    ) = with(builder) {
        entry(MainScreens.HomeNavScreen) {
            HomeScreen(
                navigateToChatListScreen = {
                    backStack.add(ChatMessageScreens.ChatListNavScreen)
                },
                navigateToProfileScreen = { _, _ ->
                    backStack.add(ProfileScreens.ProfileNavScreen("", ""))
                },
                navigateToOverviewScreen = {
                    backStack.add(ProfileScreens.ProfileOverviewNavScreen)
                }
            )
        }

        entry(MainScreens.PrivacyPolicyNavScreen) {
            PrivacyPolicyScreen {
                backStack.removeLastOrNull()
            }
        }
    }
}