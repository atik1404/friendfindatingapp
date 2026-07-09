package com.friendfinapp.dating.analytics

import com.friend.common.analytics.AnalyticsService

/**
 * Fans every [AnalyticsService] call out to multiple backends (Clarity +
 * Sentry) so business events, user context, and screen breadcrumbs are recorded
 * in both — with zero changes to the already-wired call sites (ViewModels).
 *
 * Each delegate is isolated: a failure in one never blocks the others, and no
 * call throws.
 */
class CompositeAnalyticsService(
    private val delegates: List<AnalyticsService>,
) : AnalyticsService {

    private inline fun fanOut(action: (AnalyticsService) -> Unit) {
        delegates.forEach { delegate ->
            runCatching { action(delegate) }
        }
    }

    override fun initialize() = fanOut { it.initialize() }

    override fun onAppForegrounded() = fanOut { it.onAppForegrounded() }

    override fun onAppBackgrounded() = fanOut { it.onAppBackgrounded() }

    override fun setAuthenticatedUser(userId: String, userType: String) =
        fanOut { it.setAuthenticatedUser(userId, userType) }

    override fun setUserType(userType: String) = fanOut { it.setUserType(userType) }

    override fun clearUser() = fanOut { it.clearUser() }

    override fun trackScreen(screenName: String) = fanOut { it.trackScreen(screenName) }

    override fun logEvent(event: String, params: Map<String, String>) =
        fanOut { it.logEvent(event, params) }

    override fun trackMessageSent(attachmentType: String?) =
        fanOut { it.trackMessageSent(attachmentType) }

    override fun onConnectivityChanged(isConnected: Boolean, networkType: String) =
        fanOut { it.onConnectivityChanged(isConnected, networkType) }
}
