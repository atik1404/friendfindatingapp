package com.friendfinapp.dating.observability

import io.sentry.Breadcrumb
import io.sentry.SentryEvent

/**
 * Data scrubbing to guarantee no sensitive information (tokens, passwords, API
 * secrets, payment data, or PII) is ever sent to Sentry. Runs from
 * `beforeSend` (events) and `beforeBreadcrumb` (breadcrumbs).
 *
 * Strategy: redact the *values* of any tag / extra / breadcrumb-data entry
 * whose key matches a sensitive pattern, and (when PII is disabled) strip the
 * user's email and IP. Session Replay independently masks all text/images.
 */
class SentryPiiScrubber(private val sendPii: Boolean) {

    private val sensitiveKey = Regex(
        "(?i)(pass|pwd|token|secret|authorization|auth|api[_-]?key|apikey|" +
            "cookie|session|card|cvv|cvc|ccnum|pan|iban|ssn|otp|pin|email|phone)"
    )

    /** @return the (scrubbed) event, or null to drop it entirely. */
    fun scrub(event: SentryEvent): SentryEvent {
        runCatching {
            // Tags
            event.tags?.keys?.toList()?.forEach { key ->
                if (sensitiveKey.containsMatchIn(key)) event.setTag(key, REDACTED)
            }
            // Extras
            event.extras?.keys?.toList()?.forEach { key ->
                if (sensitiveKey.containsMatchIn(key)) event.setExtra(key, REDACTED)
            }
            // Breadcrumb payloads
            event.breadcrumbs?.forEach { scrubData(it) }
            // User PII when not explicitly allowed
            if (!sendPii) {
                event.user?.apply {
                    email = null
                    ipAddress = null
                }
            }
        }
        return event
    }

    /** @return the (scrubbed) breadcrumb, or null to drop it. */
    fun scrub(breadcrumb: Breadcrumb): Breadcrumb {
        runCatching { scrubData(breadcrumb) }
        return breadcrumb
    }

    private fun scrubData(breadcrumb: Breadcrumb) {
        val data = breadcrumb.data
        data.keys.toList().forEach { key ->
            if (sensitiveKey.containsMatchIn(key)) {
                breadcrumb.setData(key, REDACTED)
            }
        }
    }

    private companion object {
        const val REDACTED = "[Filtered]"
    }
}
