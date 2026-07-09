package com.friend.common.analytics

/**
 * Keys used for event parameters and Clarity custom (session) tags.
 *
 * IMPORTANT (privacy): Only non-sensitive, non-PII values may be attached to
 * these keys. Never send raw email addresses, passwords, message bodies, phone
 * numbers, exact location, etc. See docs/ANALYTICS.md for the full policy.
 */
object AnalyticsParam {

    // ----- Session & user context (set as Clarity custom tags) -------------
    const val ANONYMOUS_USER_ID = "anonymous_user_id"
    const val USER_ID = "user_id"
    const val USER_TYPE = "user_type"
    const val APP_VERSION = "app_version"
    const val BUILD_NUMBER = "build_number"
    const val PLATFORM = "platform"
    const val DEVICE_LANGUAGE = "device_language"
    const val OS_VERSION = "os_version"
    const val SESSION_DURATION_SEC = "session_duration_sec"
    const val SCREENS_VISITED = "screens_visited"
    const val MESSAGES_SENT = "messages_sent"

    // ----- Generic event parameters ----------------------------------------
    const val SCREEN_NAME = "screen_name"
    const val METHOD = "method"
    const val SOURCE = "source"
    const val RESULT = "result"
    const val ATTACHMENT_TYPE = "attachment_type"
    const val RECIPIENT_COUNT = "recipient_count"
    const val PRODUCT_ID = "product_id"
    const val IS_CONNECTED = "is_connected"
    const val NETWORK_TYPE = "network_type"

    // ----- Diagnostics (status-code / type only, never bodies) -------------
    const val STATUS_CODE = "status_code"
    const val ENDPOINT = "endpoint"
    const val ERROR_TYPE = "error_type"
}

/** Canonical values for [AnalyticsParam.USER_TYPE]. */
object UserType {
    const val FREE = "Free"
    const val VIP = "VIP"
    const val ANONYMOUS = "Anonymous"
}

/** Canonical values for [AnalyticsParam.PLATFORM]. */
object AnalyticsPlatform {
    const val ANDROID = "Android"
}

/** Canonical values for [AnalyticsParam.ATTACHMENT_TYPE]. */
object AttachmentType {
    const val IMAGE = "Image"
    const val VIDEO = "Video"
    const val AUDIO = "Audio"
    const val FILE = "File"
}
