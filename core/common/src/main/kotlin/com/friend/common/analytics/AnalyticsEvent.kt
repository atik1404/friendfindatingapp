package com.friend.common.analytics

/**
 * Central registry of every custom analytics event name that the app reports to
 * Microsoft Clarity.
 *
 * All event names MUST be referenced from here (never hard-coded at the call
 * site) so that naming stays consistent and typo-free. Names follow a
 * "Title Case, human readable" convention because they surface as-is on the
 * Clarity dashboard's custom-event filters.
 */
object AnalyticsEvent {

    // ----- Required user actions -------------------------------------------
    const val USER_LOGIN = "User Login"
    const val USER_REGISTRATION = "User Registration"
    const val SEND_MESSAGE = "Send Message"
    const val DELETE_MESSAGE = "Delete Message"
    const val FORWARD_MESSAGE = "Forward Message"
    const val UPDATE_PROFILE = "Update Profile"
    const val LOGOUT = "Logout"
    const val VIP_MEMBERSHIP_ACTIVATED = "VIP Membership Activated"

    // ----- App / session lifecycle -----------------------------------------
    const val APP_LAUNCH = "App Launch"
    const val FIRST_APP_OPEN = "First App Open"
    const val APP_FOREGROUNDED = "App Foregrounded"
    const val APP_BACKGROUNDED = "App Backgrounded"
    const val SESSION_START = "Session Start"
    const val SESSION_END = "Session End"

    // ----- Screen / navigation ---------------------------------------------
    const val SCREEN_VIEW = "Screen View"

    // ----- High-value interactions -----------------------------------------
    const val PROFILE_VIEWED = "Profile Viewed"
    const val SEARCH_PERFORMED = "Search Performed"
    const val CONVERSATION_OPENED = "Conversation Opened"
    const val CHAT_CREATED = "Chat Created"
    const val MESSAGE_ATTACHMENT_SENT = "Message Attachment Sent"
    const val NOTIFICATION_OPENED = "Notification Opened"
    const val DEEP_LINK_OPENED = "Deep Link Opened"

    // ----- Monetization ----------------------------------------------------
    const val SUBSCRIPTION_SCREEN_VIEWED = "Subscription Screen Viewed"
    const val PURCHASE_STARTED = "Purchase Started"
    const val PURCHASE_COMPLETED = "Purchase Completed"
    const val PURCHASE_FAILED = "Purchase Failed"

    // ----- Reliability / diagnostics (non-sensitive) -----------------------
    const val ERROR_OCCURRED = "Error Occurred"
    const val API_FAILURE = "API Failure"
    const val NETWORK_CONNECTIVITY_CHANGED = "Network Connectivity Changed"
}