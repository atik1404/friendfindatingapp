package com.friendfinapp.dating.navigation.graph

import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import com.friend.changepassword.ChangePasswordScreen
import com.friend.common.constant.PersonalMenu
import com.friend.membership.MembershipScreen
import com.friend.overview.ProfileOverviewScreen
import com.friend.personalsetting.PersonalSettingScreen
import com.friend.profile.ProfileScreen
import com.friend.profilecompletion.ProfileCompletionScreen
import com.friendfinapp.dating.navigation.AuthScreens
import com.friendfinapp.dating.navigation.MainScreens
import com.friendfinapp.dating.navigation.ProfileScreens

object ProfileNavGraph {
    fun register(
        backStack: NavBackStack,
        builder: EntryProviderBuilder<NavKey>
    ) = with(builder) {
        entry(ProfileScreens.ProfileOverviewNavScreen) {
            ProfileOverviewScreen(
                onBackButtonClicked = { backStack.removeLastOrNull() },
                navigateToProfileScreen = {
                    backStack.add(ProfileScreens.ProfileNavScreen("", ""))
                },
                clickedOnMenu = { menu ->
                    when (menu) {
                        PersonalMenu.PERSONAL_SETTING -> backStack.add(ProfileScreens.PersonalSettingNavScreen)
                        PersonalMenu.PRIVACY_POLICY -> backStack.add(MainScreens.PrivacyPolicyNavScreen)
                        PersonalMenu.SHARE_APP -> {}
                        PersonalMenu.RATE_APP -> {}
                        PersonalMenu.CHANGE_PASSWORD -> backStack.add(ProfileScreens.ChangePasswordNavScreen)
                        PersonalMenu.CONTACT_US -> {}
                        PersonalMenu.VIP_MEMBERSHIP -> backStack.add(ProfileScreens.MembershipNavScreen)
                        PersonalMenu.LOGOUT -> {
                            backStack.clear()
                            backStack.add(AuthScreens.LoginNavScreen)
                        }
                    }
                }
            )
        }
        entry<ProfileScreens.ProfileNavScreen> { key ->
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
            ProfileCompletionScreen(
                onBackButtonClicked = { backStack.removeLastOrNull() },
                onContinueButtonClicked = {
                    backStack.clear()
                    backStack.add(MainScreens.HomeNavScreen)
                }
            )
        }

        entry(ProfileScreens.PersonalSettingNavScreen) {
            PersonalSettingScreen {
                backStack.removeLastOrNull()
            }
        }

        entry(ProfileScreens.ChangePasswordNavScreen) {
            ChangePasswordScreen {
                backStack.removeLastOrNull()
            }
        }

        entry(ProfileScreens.MembershipNavScreen) {
            MembershipScreen {
                backStack.removeLastOrNull()
            }
        }
    }
}