package com.friendfinapp.dating.observability

import com.friend.common.observability.LogSeverity
import com.friend.common.observability.MonitoringService
import io.sentry.Breadcrumb
import io.sentry.Sentry
import io.sentry.SentryAttribute
import io.sentry.SentryAttributes
import io.sentry.SentryLevel
import io.sentry.SentryLogLevel
import io.sentry.SpanStatus
import io.sentry.logger.SentryLogParameters
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Sentry-backed [MonitoringService]: non-fatal exception capture, structured
 * logs, manual breadcrumbs, tags, and custom performance transactions.
 *
 * Every method is guarded and non-throwing (except [trace], which must
 * propagate the caller's exception after recording it). All calls are safe
 * no-ops until Sentry is initialized.
 */
@Singleton
class SentryMonitoringService @Inject constructor() : MonitoringService {

    override fun captureException(throwable: Throwable, context: Map<String, String>) {
        runCatching {
            Sentry.withScope { scope ->
                context.forEach { (key, value) -> scope.setTag(key, value) }
                Sentry.captureException(throwable)
            }
        }
    }

    override fun log(
        level: LogSeverity,
        message: String,
        category: String?,
        attributes: Map<String, String>,
    ) {
        runCatching {
            val attrs = buildList {
                category?.let { add(SentryAttribute.stringAttribute("category", it)) }
                attributes.forEach { (k, v) -> add(SentryAttribute.stringAttribute(k, v)) }
            }
            val params = SentryLogParameters.create(SentryAttributes.of(*attrs.toTypedArray()))
            Sentry.logger().log(level.toLogLevel(), params, message)
        }
    }

    override fun addBreadcrumb(
        message: String,
        category: String,
        level: LogSeverity,
        data: Map<String, String>,
    ) {
        runCatching {
            val crumb = Breadcrumb().apply {
                this.message = message
                this.category = category
                this.level = level.toSentryLevel()
                data.forEach { (k, v) -> setData(k, v) }
            }
            Sentry.addBreadcrumb(crumb)
        }
    }

    override fun setTag(key: String, value: String) {
        runCatching { Sentry.setTag(key, value) }
    }

    override fun <T> trace(name: String, operation: String, block: () -> T): T {
        val transaction = runCatching { Sentry.startTransaction(name, operation) }.getOrNull()
        return try {
            val result = block()
            runCatching { transaction?.finish(SpanStatus.OK) }
            result
        } catch (t: Throwable) {
            runCatching {
                transaction?.apply {
                    throwable = t
                    finish(SpanStatus.INTERNAL_ERROR)
                }
            }
            throw t
        }
    }

    private fun LogSeverity.toLogLevel(): SentryLogLevel = when (this) {
        LogSeverity.DEBUG -> SentryLogLevel.DEBUG
        LogSeverity.INFO -> SentryLogLevel.INFO
        LogSeverity.WARNING -> SentryLogLevel.WARN
        LogSeverity.ERROR -> SentryLogLevel.ERROR
        LogSeverity.FATAL -> SentryLogLevel.FATAL
    }

    private fun LogSeverity.toSentryLevel(): SentryLevel = when (this) {
        LogSeverity.DEBUG -> SentryLevel.DEBUG
        LogSeverity.INFO -> SentryLevel.INFO
        LogSeverity.WARNING -> SentryLevel.WARNING
        LogSeverity.ERROR -> SentryLevel.ERROR
        LogSeverity.FATAL -> SentryLevel.FATAL
    }
}
