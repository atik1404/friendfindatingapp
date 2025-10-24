package com.friendfinapp.dating.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay

@Composable
fun AppNavConfiguration() {
    val backStack = rememberNavBackStack(AuthScreens.SplashNavScreen).toMutableStateList()
    val navResults = remember { NavResultManager() }

    CompositionLocalProvider(
        LocalNavResultManager provides navResults
    ) {
        NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            transitionSpec = {
                val dur = 700
                (slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(dur)
                ) + fadeIn(tween(dur)))
                    .togetherWith(
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Left,
                            tween(dur)
                        ) + fadeOut(tween(dur))
                    )
            },

            // POP (back) animation
            popTransitionSpec = {
                val dur = 700
                (slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(dur)
                ) + fadeIn(tween(dur)))
                    .togetherWith(
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Right,
                            tween(dur)
                        ) + fadeOut(tween(dur))
                    )
            },
            entryProvider = entryProvider {
                AuthNavGraph.register(backStack, this)
            }
        )
    }
}