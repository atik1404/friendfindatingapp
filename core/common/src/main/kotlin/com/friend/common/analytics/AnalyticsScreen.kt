package com.friend.common.analytics

/**
 * Human-readable, stable screen names used for screen-view analytics.
 *
 * These are decoupled from navigation/route class names so that refactors to
 * navigation don't silently change analytics, and so the Clarity dashboard
 * shows friendly names. The mapping from navigation keys to these names lives
 * in the app module's screen-name mapper.
 */
object AnalyticsScreen {
    const val SPLASH = "Splash"
    const val LOGIN = "Login"
    const val REGISTRATION = "Registration"
    const val FORGOT_PASSWORD = "Forgot Password"

    const val HOME = "Home"
    const val PRIVACY_POLICY = "Privacy Policy"
    const val VIDEO_PLAYER = "Video Player"

    const val CHAT_LIST = "Chat List"
    const val CONVERSATION = "Conversation"
    const val FORWARD_MESSAGE = "Forward Message"

    const val PROFILE_OVERVIEW = "Profile Overview"
    const val MY_PROFILE = "My Profile"
    const val OTHER_PROFILE = "Other Profile"
    const val PROFILE_COMPLETION = "Profile Completion"
    const val PERSONAL_SETTING = "Personal Setting"
    const val CHANGE_PASSWORD = "Change Password"
    const val VIP_MEMBERSHIP = "VIP Membership"
    const val REPORT_USER = "Report User"

    const val UNKNOWN = "Unknown"
}
