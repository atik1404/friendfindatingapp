package com.friendfinapp.dating.navigation.graph

import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import com.friend.common.constant.PersonalMenu
import com.friend.overview.ProfileOverviewScreen
import com.friend.personalsetting.PersonalSettingScreen
import com.friend.profile.ProfileScreen
import com.friend.profilecompletion.ProfileCompletionScreen
import com.friendfinapp.dating.navigation.AuthScreens
import com.friendfinapp.dating.navigation.ProfileScreens

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
                clickedOnMenu = { menu ->
                    when(menu){
                        PersonalMenu.PERSONAL_SETTING -> backStack.add(ProfileScreens.PersonalSettingNavScreen)
                        PersonalMenu.PRIVACY_POLICY -> {}
                        PersonalMenu.SHARE_APP -> {}
                        PersonalMenu.RATE_APP -> {}
                        PersonalMenu.CHANGE_PASSWORD -> {}
                        PersonalMenu.CONTACT_US -> {}
                        PersonalMenu.LOGOUT -> {
                            backStack.clear()
                            backStack.add(AuthScreens.LoginNavScreen)
                        }
                    }
                }
            )
        }
        entry<ProfileScreens.ProfileNavScreen> { key->
            ProfileScreen(
                username = key.userName,
                userId = key.userId,
                navigateToEditProfile = {
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

        entry(ProfileScreens.PersonalSettingNavScreen) {
            PersonalSettingScreen {
                backStack.removeLastOrNull()
            }
        }
    }
}