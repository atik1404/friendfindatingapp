package com.friendfinapp.dating.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface AppScreen : NavKey

sealed interface Main : AppScreen {
    @Serializable
    object SplashScreen : AppScreen
}