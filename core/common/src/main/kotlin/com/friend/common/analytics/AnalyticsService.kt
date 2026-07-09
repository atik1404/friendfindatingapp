package com.friend.common.analytics

/**
 * Central abstraction for all analytics / session-recording operations.
 *
 * UI, ViewModels, and infrastructure code depend ONLY on this interface — never
 * on the Microsoft Clarity SDK directly. The concrete Clarity-backed
 * implementation is provided via dependency injection (see the app module's
 * `ClarityAnalyticsManager` + `AnalyticsModule`).
 *
 * All implementations must be safe to call from any thread and must never throw
 * (analytics must never crash the app) — the underlying SDK calls are marshalled
 * to the main thread and guarded internally.
 */
interface AnalyticsService {

    /**
     * Initialize the analytics SDK. Must be called once from application
     * startup. Repeated calls are ignored so it is safe against races.
     */
    fun initialize()

    // ----- App / session lifecycle -----------------------------------------

    /** The app moved to the foreground (also opens a new logical session). */
    fun onAppForegrounded()

    /** The app moved to the background (also closes the logical session). */
    fun onAppBackgrounded()

    // ----- User & session context ------------------------------------------

    /**
     * Associate the authenticated user with the session.
     *
     * @param userId a stable, NON-PII identifier (e.g. username/handle, never
     * an email or full name).
     * @param userType one of [UserType].
     */
    fun setAuthenticatedUser(userId: String, userType: String)

    /** Update just the membership tier tag (e.g. after a VIP purchase). */
    fun setUserType(userType: String)

    /** Clear any authenticated-user association (logout). */
    fun clearUser()

    // ----- Screens ----------------------------------------------------------

    /**
     * Report that the user landed on [screenName]. Implementations must
     * de-duplicate consecutive identical screen names so configuration changes
     * / recompositions don't emit duplicate events.
     */
    fun trackScreen(screenName: String)

    // ----- Events -----------------------------------------------------------

    /**
     * Log a custom event.
     *
     * @param event one of [AnalyticsEvent].
     * @param params optional NON-PII parameters (keys from [AnalyticsParam]).
     */
    fun logEvent(event: String, params: Map<String, String> = emptyMap())

    /**
     * Record that the user sent a message. Increments the session
     * "messages sent" counter in addition to logging the event.
     */
    fun trackMessageSent(attachmentType: String? = null)

    /** Report a connectivity change (used both for the tag and an event). */
    fun onConnectivityChanged(isConnected: Boolean, networkType: String)
}
