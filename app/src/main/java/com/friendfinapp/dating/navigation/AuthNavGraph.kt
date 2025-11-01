package com.friendfinapp.dating.navigation


import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import com.friend.forgotpassword.ForgotPasswordScreen
import com.friend.login.LoginScreen
import com.friend.profilecompletion.ProfileCompletionScreen
import com.friend.registration.RegistrationScreen
import com.friend.splash.SplashScreen

object AuthNavGraph {
    fun register(
        backStack: NavBackStack,
        builder: EntryProviderBuilder<NavKey>
    ) = with(builder) {
        entry(AuthScreens.SplashNavScreen) {
            SplashScreen(
                navigateToLoginScreen = {
                    backStack.clear()
                    backStack.add(AuthScreens.LoginNavScreen)
                },
                navigateToHomeScreen = {
                    //backStack.add(Main.HomeScreen)
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
                navigateToProfileCompletion = {
                    backStack.add(AuthScreens.ProfileCompletionNavScreen)
                }
            )
        }

        entry(AuthScreens.ForgotPasswordNavScreen) {
            ForgotPasswordScreen{
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
                    backStack.add(AuthScreens.ProfileCompletionNavScreen)
                }
            )
        }

        entry(AuthScreens.ProfileCompletionNavScreen) {
            ProfileCompletionScreen {
                backStack.removeLastOrNull()
            }
        }
    }
}
