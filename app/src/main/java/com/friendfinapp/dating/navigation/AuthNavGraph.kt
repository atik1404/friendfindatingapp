package com.friendfinapp.dating.navigation


import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import com.friend.login.LoginScreen
import com.friend.splash.SplashScreen

object AuthNavGraph {
    fun register(
        backStack: NavBackStack,
        builder: EntryProviderBuilder<NavKey>
    ) = with(builder) {
        entry(AuthScreens.SplashNavScreen) {
            SplashScreen(
                navigateToLoginScreen = {
                    backStack.add(AuthScreens.LoginNavScreen)
                },
                navigateToHomeScreen = {
                    //backStack.add(Main.HomeScreen)
                }
            )
        }

        entry(AuthScreens.LoginNavScreen) {
             LoginScreen()
        }
    }
}
