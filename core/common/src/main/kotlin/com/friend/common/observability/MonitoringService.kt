package com.friend.common.observability

/**
 * Severity levels for structured logs and captured messages, mapped by the
 * implementation onto the backend's native levels (e.g. Sentry).
 */
enum class LogSeverity { DEBUG, INFO, WARNING, ERROR, FATAL }

/**
 * Application-wide observability facade (crash reporting, structured logging,
 * breadcrumbs, and custom performance tracing).
 *
 * This is the ONLY type infrastructure/ViewModels depend on — never the Sentry
 * SDK directly. The concrete implementation is provided via DI (see the app
 * module's `SentryMonitoringService` + `AnalyticsModule`).
 *
 * Business analytics events, user context, and screen breadcrumbs are handled
 * through [com.friend.common.analytics.AnalyticsService] (which fans out to
 * Sentry as well), so this facade focuses on the reliability/performance side:
 * exceptions, logs, and spans.
 *
 * All methods must be safe to call from any thread, must never throw, and must
 * be no-ops until Sentry is initialized.
 */
interface MonitoringService {

    /**
     * Report a non-fatal (caught) exception with optional extra context.
     *
     * @param context NON-PII key/values (e.g. module, endpoint, status_code).
     */
    fun captureException(throwable: Throwable, context: Map<String, String> = emptyMap())

    /**
     * Emit a structured log at [level] with optional NON-PII attributes.
     * Also useful as a lightweight audit trail for important actions.
     */
    fun log(
        level: LogSeverity,
        message: String,
        category: String? = null,
        attributes: Map<String, String> = emptyMap(),
    )

    /** Manually record a breadcrumb (trail of events leading up to an error). */
    fun addBreadcrumb(
        message: String,
        category: String,
        level: LogSeverity = LogSeverity.INFO,
        data: Map<String, String> = emptyMap(),
    )

    /** Attach/replace a NON-PII tag applied to all subsequent events. */
    fun setTag(key: String, value: String)

    /**
     * Wrap [block] in a performance transaction/span so its duration and status
     * are reported. Exceptions propagate but are recorded on the span first.
     *
     * @param operation coarse category, e.g. "db", "task", "ui.load".
     */
    fun <T> trace(name: String, operation: String, block: () -> T): T
}
