package com.friendfinapp.dating.navigation.graph

import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import com.friend.overview.ProfileOverviewScreen
import com.friend.profile.ProfileScreen
import com.friend.profilecompletion.ProfileCompletionScreen
import com.friendfinapp.dating.navigation.AuthScreens
import com.friendfinapp.dating.navigation.ProfileScreens
import timber.log.Timber

object ProfileNavGraph {
    fun register(
        backStack: NavBackStack,
        builder: EntryProviderBuilder<NavKey>
    ) = with(builder) {
        entry(ProfileScreens.ProfileOverviewNavScreen) {
            ProfileOverviewScreen(
                onBackButtonClicked = {backStack.removeLastOrNull()},
                navigateToProfileScreen = {
                    backStack.add(ProfileScreens.ProfileNavScreen("", ""))
                },
                navigateToLogoutScreen = {
                    backStack.clear()
                    backStack.add(AuthScreens.LoginNavScreen)
                }
            )
        }
        entry<ProfileScreens.ProfileNavScreen> { key->
            ProfileScreen(
                username = key.userName,
                userId = key.userId,
                navigateToEditProfile = {
                    Timber.e("navigate to profile completion")
                    backStack.add(ProfileScreens.ProfileCompletionNavScreen)
                },
                onBackButtonClicked = {
                    backStack.removeLastOrNull()
                }
            )
        }
        entry(ProfileScreens.ProfileCompletionNavScreen) {
            ProfileCompletionScreen {
                backStack.removeLastOrNull()
            }
        }
    }
}