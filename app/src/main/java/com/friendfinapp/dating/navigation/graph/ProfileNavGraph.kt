package com.friendfinapp.dating.navigation.graph

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.friend.changepassword.ChangePasswordScreen
import com.friend.common.constant.PersonalMenu
import com.friend.membership.MembershipScreen
import com.friend.overview.ProfileOverviewRoute
import com.friend.overview.ProfileOverviewScreen
import com.friend.personalsetting.PersonalSettingScreen
import com.friend.profile.ProfileScreen
import com.friend.profilecompletion.ProfileCompletionScreen
import com.friend.reportabuse.ReportAbuseScreen
import com.friendfinapp.dating.navigation.AuthScreens
import com.friendfinapp.dating.navigation.ChatMessageScreens
import com.friendfinapp.dating.navigation.MainScreens
import com.friendfinapp.dating.navigation.ProfileScreens

object ProfileNavGraph {
    fun register(
        backStack: NavBackStack<NavKey>,
        builder: EntryProviderScope<NavKey>
    ) = with(builder) {

        entry(ProfileScreens.ProfileOverviewNavScreen) {
            ProfileOverviewRoute(
                onBackButtonClicked = { backStack.removeLastOrNull() },
                navigateToLoginScreen = {
                    backStack.clear()
                    backStack.add(AuthScreens.LoginNavScreen)
                },
                navigateToProfileScreen = {
                    backStack.add(ProfileScreens.ProfileNavScreen("", ""))
                },
                clickedOnMenu = { menu ->
                    when (menu) {
                        PersonalMenu.PERSONAL_SETTING -> backStack.add(ProfileScreens.PersonalSettingNavScreen)
                        PersonalMenu.PRIVACY_POLICY -> backStack.add(MainScreens.PrivacyPolicyNavScreen)
                        PersonalMenu.CHANGE_PASSWORD -> backStack.add(ProfileScreens.ChangePasswordNavScreen)
                        PersonalMenu.VIP_MEMBERSHIP -> backStack.add(ProfileScreens.MembershipNavScreen)
                        PersonalMenu.LOGOUT -> {
                            backStack.clear()
                            backStack.add(AuthScreens.LoginNavScreen)
                        }

                        else -> {}
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
                },
                navigateToMessageRoom = {
                    backStack.add(ChatMessageScreens.ChatRoomNavScreen("", "Tom Cruise"))
                },
                navigateToReportAbuse = {
                    backStack.add(ProfileScreens.ReportUserNavScreen)
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

        entry(ProfileScreens.ReportUserNavScreen) {
            ReportAbuseScreen {
                backStack.removeLastOrNull()
            }
        }
    }
}