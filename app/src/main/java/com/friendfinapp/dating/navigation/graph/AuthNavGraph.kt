package com.friendfinapp.dating.navigation.graph

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.friend.forgotpassword.ForgotPasswordScreen
import com.friend.login.LoginScreen
import com.friend.registration.RegistrationScreen
import com.friend.splash.SplashScreen
import com.friendfinapp.dating.navigation.AuthScreens
import com.friendfinapp.dating.navigation.MainScreens
import com.friendfinapp.dating.navigation.ProfileScreens

object AuthNavGraph {
    fun register(
        backStack: NavBackStack<NavKey>,
        builder: EntryProviderScope<NavKey>
    ) = with(builder) {
        entry(AuthScreens.SplashNavScreen) {
            SplashScreen(
                navigateToLoginScreen = {
                    backStack.clear()
                    backStack.add(AuthScreens.LoginNavScreen)
                },
                navigateToHomeScreen = {
                    backStack.add(MainScreens.HomeNavScreen)
                }
            )
        }

        entry(AuthScreens.LoginNavScreen) {
            LoginScreen(
                navigateToRegistration = {
                    backStack.add(AuthScreens.RegistrationNavScreen)
                },
                navigateToForgotPassword = {
                    backStack.add(AuthScreens.ForgotPasswordNavScreen)
                },
                navigateToHome = {
                    backStack.clear()
                    backStack.add(MainScreens.HomeNavScreen)
                }
            )
        }

        entry(AuthScreens.ForgotPasswordNavScreen) {
            ForgotPasswordScreen {
                backStack.removeLastOrNull()
            }
        }

        entry(AuthScreens.RegistrationNavScreen) {
            RegistrationScreen(
                onBackButtonClicked = {
                    backStack.removeLastOrNull()
                },
                navigateToProfileCompletion = {
                    backStack.remove(AuthScreens.RegistrationNavScreen)
                    backStack.add(ProfileScreens.ProfileCompletionNavScreen)
                }
            )
        }
    }
}
