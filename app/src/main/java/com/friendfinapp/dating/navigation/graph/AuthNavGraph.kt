package com.friendfinapp.dating.navigation.graph

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.friend.forgotpassword.ForgotPasswordRoute
import com.friend.login.LoginRoute
import com.friend.registration.RegistrationRoute
import com.friend.splash.SplashRoute
import com.friendfinapp.dating.navigation.AuthScreens
import com.friendfinapp.dating.navigation.MainScreens
import com.friendfinapp.dating.navigation.ProfileScreens

object AuthNavGraph {
    fun register(
        backStack: NavBackStack<NavKey>,
        builder: EntryProviderScope<NavKey>,
    ) = with(builder) {
        entry(AuthScreens.SplashNavScreen) {
            SplashRoute(
                navigateToLoginScreen = {
                    backStack.clear()
                    backStack.add(AuthScreens.LoginNavScreen)
                },
                navigateToHomeScreen = {
                    backStack.clear()
                    backStack.add(MainScreens.HomeNavScreen)
                },
                navigateToProfileComplete = {
                    backStack.clear()
                    backStack.add(ProfileScreens.ProfileCompletionNavScreen)
                }
            )
        }

        entry(AuthScreens.LoginNavScreen) {
            LoginRoute(
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
            ForgotPasswordRoute(
                onNavigateBack = {
                    backStack.removeLastOrNull()
                }
            )
        }

        entry(AuthScreens.RegistrationNavScreen) {
            RegistrationRoute(
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
