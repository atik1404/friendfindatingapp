package com.friendfinapp.dating.navigation.graph

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.friend.home.HomeRoute
import com.friend.policy.PrivacyPolicyScreen
import com.friend.videoplayer.VideoPlayerScreenRoute
import com.friendfinapp.dating.navigation.ChatMessageScreens
import com.friendfinapp.dating.navigation.CommonScreens
import com.friendfinapp.dating.navigation.ProfileScreens

object CommonNavGraph {
    fun register(
        backStack: NavBackStack<NavKey>,
        builder: EntryProviderScope<NavKey>
    ) = with(builder) {
        entry(CommonScreens.HomeNavScreen) {
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
                },
                navigateToMembershipScreen = {
                    backStack.add(ProfileScreens.MembershipNavScreen)
                }
            )
        }

        entry(CommonScreens.PrivacyPolicyNavScreen) {
            PrivacyPolicyScreen {
                backStack.removeLastOrNull()
            }
        }

        entry<CommonScreens.VideoPlayerNavScreen> { key ->
            VideoPlayerScreenRoute(key.url) {
                backStack.removeLastOrNull()
            }
        }
    }
}