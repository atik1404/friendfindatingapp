package com.friendfinapp.dating.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface AppScreen : NavKey

sealed interface AuthScreens : AppScreen {
    @Serializable
    object SplashNavScreen : AppScreen

    @Serializable
    object LoginNavScreen : AppScreen

    @Serializable
    object RegistrationNavScreen : AppScreen

    @Serializable
    object ForgotPasswordNavScreen : AppScreen
}

sealed interface MainScreens : AppScreen {
    @Serializable
    object HomeNavScreen : AppScreen

    @Serializable
    object PrivacyPolicyNavScreen : AppScreen
}

sealed interface ChatMessageScreens : AppScreen {
    @Serializable
    object ChatListNavScreen : AppScreen

    @Serializable
    data class ChatRoomNavScreen(
        val messageId: String,
        val userName: String
    ) : AppScreen
}

sealed interface ProfileScreens : AppScreen {
    @Serializable
    object ProfileOverviewNavScreen : AppScreen

    @Serializable
    data class ProfileNavScreen(
        val userId: String,
        val userName: String
    ) : AppScreen

    @Serializable
    object ProfileCompletionNavScreen : AppScreen

    @Serializable
    object PersonalSettingNavScreen : AppScreen

    @Serializable
    object ChangePasswordNavScreen : AppScreen
    @Serializable
    object MembershipNavScreen : AppScreen

    @Serializable
    object ReportUserNavScreen : AppScreen
}