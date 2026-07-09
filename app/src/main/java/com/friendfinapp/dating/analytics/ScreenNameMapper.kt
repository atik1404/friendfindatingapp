package com.friendfinapp.dating.analytics

import androidx.navigation3.runtime.NavKey
import com.friend.common.analytics.AnalyticsScreen
import com.friendfinapp.dating.navigation.AuthScreens
import com.friendfinapp.dating.navigation.ChatMessageScreens
import com.friendfinapp.dating.navigation.CommonScreens
import com.friendfinapp.dating.navigation.ProfileScreens

/**
 * Maps a navigation destination key to a stable, human-readable analytics
 * screen name. Kept in one place so screen-view tracking stays consistent and
 * independent of navigation refactors.
 */
fun NavKey.toAnalyticsScreenName(): String = when (this) {
    is AuthScreens.SplashNavScreen -> AnalyticsScreen.SPLASH
    is AuthScreens.LoginNavScreen -> AnalyticsScreen.LOGIN
    is AuthScreens.RegistrationNavScreen -> AnalyticsScreen.REGISTRATION
    is AuthScreens.ForgotPasswordNavScreen -> AnalyticsScreen.FORGOT_PASSWORD

    is CommonScreens.HomeNavScreen -> AnalyticsScreen.HOME
    is CommonScreens.PrivacyPolicyNavScreen -> AnalyticsScreen.PRIVACY_POLICY
    is CommonScreens.VideoPlayerNavScreen -> AnalyticsScreen.VIDEO_PLAYER

    is ChatMessageScreens.ChatListNavScreen -> AnalyticsScreen.CHAT_LIST
    is ChatMessageScreens.ConversationNavScreen -> AnalyticsScreen.CONVERSATION
    is ChatMessageScreens.ForwardMessageNavScreen -> AnalyticsScreen.FORWARD_MESSAGE

    is ProfileScreens.ProfileOverviewNavScreen -> AnalyticsScreen.PROFILE_OVERVIEW
    is ProfileScreens.ProfileNavScreen -> AnalyticsScreen.MY_PROFILE
    is ProfileScreens.OtherProfileNavScreen -> AnalyticsScreen.OTHER_PROFILE
    is ProfileScreens.ProfileCompletionNavScreen -> AnalyticsScreen.PROFILE_COMPLETION
    is ProfileScreens.PersonalSettingNavScreen -> AnalyticsScreen.PERSONAL_SETTING
    is ProfileScreens.ChangePasswordNavScreen -> AnalyticsScreen.CHANGE_PASSWORD
    is ProfileScreens.MembershipNavScreen -> AnalyticsScreen.VIP_MEMBERSHIP
    is ProfileScreens.ReportUserNavScreen -> AnalyticsScreen.REPORT_USER

    else -> AnalyticsScreen.UNKNOWN
}
