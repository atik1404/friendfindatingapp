package com.friendfinapp.dating.navigation.graph

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.friend.home.HomeRoute
import com.friend.home.HomeScreen
import com.friend.policy.PrivacyPolicyScreen
import com.friendfinapp.dating.navigation.ChatMessageScreens
import com.friendfinapp.dating.navigation.MainScreens
import com.friendfinapp.dating.navigation.ProfileScreens

object MainNavGraph {
    fun register(
        backStack: NavBackStack<NavKey>,
        builder: EntryProviderScope<NavKey>
    ) = with(builder) {
        entry(MainScreens.HomeNavScreen) {
            HomeRoute(
                navigateToChatListScreen = {
                    backStack.add(ChatMessageScreens.ChatListNavScreen)
                },
                navigateToProfileScreen = {
                    backStack.add(ProfileScreens.ProfileNavScreen)
                },
                navigateToOverviewScreen = {
                    backStack.add(ProfileScreens.ProfileOverviewNavScreen)
                },
                navigateToOtherProfileScreen = { username ->
                    backStack.add(ProfileScreens.OtherProfileNavScreen(username))
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