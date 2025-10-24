package com.friendfinapp.dating.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface AppScreen : NavKey

sealed interface AuthScreens : AppScreen {
    @Serializable
    object SplashNavScreen : AppScreen

    @Serializable
    object LoginNavScreen : AppScreen
    object RegistrationNavScreen : AppScreen
    object ForgotPasswordNavScreen : AppScreen
    object ProfileCompletionNavScreen : AppScreen
}