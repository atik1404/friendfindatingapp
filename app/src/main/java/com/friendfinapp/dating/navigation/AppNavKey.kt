package com.friendfinapp.dating.navigation

import androidx.navigation3.runtime.NavKey
import com.friend.entity.chatmessage.ChatItemApiEntity
import kotlinx.serialization.Serializable

sealed interface AppScreen : NavKey

sealed interface AuthScreens : AppScreen {
    @Serializable
    object SplashNavScreen : AppScreen

    @Serializable
    object LoginNavScreen : AppScreen

    @Serializable
    data class RegistrationNavScreen(val email: String) : AppScreen

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
    data class ConversationNavScreen(
        val toUsername: String,
        val fullName: String,
        val imageUrl: String
    ) : AppScreen

    @Serializable
    data class ForwardMessageNavScreen(
        val messages: List<String>,
    ) : AppScreen
}

sealed interface ProfileScreens : AppScreen {
    @Serializable
    object ProfileOverviewNavScreen : AppScreen

    @Serializable
    data object ProfileNavScreen : AppScreen

    @Serializable
    data class OtherProfileNavScreen(
        val username: String,
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
    data class ReportUserNavScreen(val username: String) : AppScreen
}