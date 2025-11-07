package com.friendfinapp.dating.navigation.graph

import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import com.friend.overview.ProfileOverviewScreen
import com.friend.profile.ProfileScreen
import com.friend.profilecompletion.ProfileCompletionScreen
import com.friendfinapp.dating.navigation.ProfileScreens

object ProfileNavGraph {
    fun register(
        backStack: NavBackStack,
        builder: EntryProviderBuilder<NavKey>
    ) = with(builder) {
        entry(ProfileScreens.ProfileOverviewNavScreen) {
            ProfileOverviewScreen {
                backStack.removeLastOrNull()
            }
        }
        entry<ProfileScreens.ProfileNavScreen> { key->
            ProfileScreen(key.userName, key.userId) {
                backStack.removeLastOrNull()
            }
        }
        entry(ProfileScreens.ProfileCompletionNavScreen) {
            ProfileCompletionScreen {
                backStack.removeLastOrNull()
            }
        }
    }
}