package com.friendfinapp.dating.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.friend.common.analytics.AnalyticsService
import com.friendfinapp.dating.analytics.toAnalyticsScreenName
import com.friendfinapp.dating.navigation.graph.AuthNavGraph
import com.friendfinapp.dating.navigation.graph.ChatMessageNavGraph
import com.friendfinapp.dating.navigation.graph.CommonNavGraph
import com.friendfinapp.dating.navigation.graph.ProfileNavGraph

@Composable
fun AppNavConfiguration(analytics: AnalyticsService) {
    val backStack = rememberNavBackStack(AuthScreens.SplashNavScreen)
    val navResults = remember { NavResultManager() }

    // Central, deduplicated screen tracking: fires only when the top of the
    // back stack actually changes. Because `backStack` is saved/restored, this
    // does NOT re-fire on configuration changes or recompositions.
    val currentKey = backStack.lastOrNull()
    LaunchedEffect(currentKey) {
        currentKey?.let { analytics.trackScreen(it.toAnalyticsScreenName()) }
    }

    CompositionLocalProvider(
        LocalNavResultManager provides navResults
    ) {
        NavDisplay(
            backStack = backStack,
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
            ),
            onBack = { backStack.removeLastOrNull() },
            transitionSpec = {
                val duration = 700
                (slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(duration)
                ) + fadeIn(tween(duration)))
                    .togetherWith(
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Left,
                            tween(duration)
                        ) + fadeOut(tween(duration))
                    )
            },

            // POP (back) animation
            popTransitionSpec = {
                val duration = 700
                (slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(duration)
                ) + fadeIn(tween(duration)))
                    .togetherWith(
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Right,
                            tween(duration)
                        ) + fadeOut(tween(duration))
                    )
            },
            entryProvider = entryProvider {
                AuthNavGraph.register(backStack, this)
                CommonNavGraph.register(backStack, this)
                ChatMessageNavGraph.register(backStack, this)
                ProfileNavGraph.register(backStack, this)
            }
        )
    }
}