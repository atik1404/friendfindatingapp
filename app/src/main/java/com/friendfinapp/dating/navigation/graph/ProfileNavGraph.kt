package com.friendfinapp.dating.navigation.graph

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.friend.changepassword.ChangePasswordScreen
import com.friend.membership.MembershipScreen
import com.friend.otherprofile.OtherProfileScreenRouter
import com.friend.overview.ProfileOverviewRoute
import com.friend.personalsetting.PersonalSettingScreen
import com.friend.profile.ProfileScreenRouter
import com.friend.profilecompletion.ProfileCompletionScreenRoute
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
                    backStack.add(ProfileScreens.ProfileNavScreen)
                },
                navigateToMembershipScreen = { backStack.add(ProfileScreens.MembershipNavScreen) },
                navigateToPrivacyPolicyScreen = { backStack.add(MainScreens.PrivacyPolicyNavScreen) },
                navigateToChangePasswordScreen = { backStack.add(ProfileScreens.ChangePasswordNavScreen) },
                navigateToPersonalSettingScreen = { backStack.add(ProfileScreens.PersonalSettingNavScreen) },
            )
        }
        entry<ProfileScreens.ProfileNavScreen> { key ->
            ProfileScreenRouter(
                navigateToEditProfile = {
                    backStack.add(ProfileScreens.ProfileCompletionNavScreen)
                },
                onBackButtonClicked = {
                    backStack.removeLastOrNull()
                },
            )
        }

        entry<ProfileScreens.OtherProfileNavScreen> { key ->
            OtherProfileScreenRouter(
                username = key.username,
                onBackButtonClicked = {
                    backStack.removeLastOrNull()
                },
                onNavigateToMessageRoom = {
                    backStack.add(ChatMessageScreens.ChatListNavScreen)
                },
                navigateToReportAbuse = {
                    backStack.add(ProfileScreens.ReportUserNavScreen)
                }
            )
        }

        entry(ProfileScreens.ProfileCompletionNavScreen) {
            ProfileCompletionScreenRoute(
                onBackButtonClicked = {
                    if (backStack.isNotEmpty())
                        backStack.removeLastOrNull()
                },
                navigateToHomeScreen = {
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