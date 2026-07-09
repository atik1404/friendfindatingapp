package com.friend.common.observability

/**
 * Stable keys/categories for observability context so tags, breadcrumb
 * categories, and log attributes stay consistent and typo-free.
 */
object ObservabilityKey {
    // Tags / context keys
    const val MODULE = "module"
    const val FEATURE = "feature"
    const val ENDPOINT = "endpoint"
    const val STATUS_CODE = "status_code"
    const val ERROR_TYPE = "error_type"
    const val NETWORK_STATUS = "network_status"
    const val REQUEST_ID = "request_id"
    const val USER_TYPE = "user_type"
    const val SCREEN = "screen"
}

/** Canonical breadcrumb categories. */
object BreadcrumbCategory {
    const val NAVIGATION = "navigation"
    const val AUTH = "auth"
    const val NETWORK = "http"
    const val UI = "ui.interaction"
    const val BUSINESS = "business"
    const val LIFECYCLE = "app.lifecycle"
    const val SYSTEM = "system"
}
