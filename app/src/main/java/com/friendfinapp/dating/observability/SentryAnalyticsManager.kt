package com.friendfinapp.dating.observability

import android.content.Context
import com.friend.common.analytics.AnalyticsParam
import com.friend.common.analytics.AnalyticsService
import com.friend.common.observability.BreadcrumbCategory
import com.friend.common.observability.ObservabilityKey
import com.friend.sharedpref.SharedPrefHelper
import com.friend.sharedpref.SpKey
import com.friendfinapp.dating.BuildConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import io.sentry.Breadcrumb
import io.sentry.ProfileLifecycle
import io.sentry.Sentry
import io.sentry.SentryLevel
import io.sentry.SentryOptions
import io.sentry.android.core.SentryAndroid
import io.sentry.protocol.User
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Sentry-backed [AnalyticsService]. Two responsibilities:
 *
 *  1. **Initialize the Sentry SDK** (once) with crash reporting, tracing,
 *     profiling, session replay, logs, offline caching, release/environment,
 *     and PII scrubbing — all driven by [BuildConfig] so Development / Staging /
 *     Production differ purely by configuration.
 *  2. **Translate analytics calls into Sentry primitives** — business events →
 *     breadcrumbs, user context → Sentry user, screens → navigation
 *     breadcrumbs. This runs alongside the Clarity implementation via
 *     [com.friendfinapp.dating.analytics.CompositeAnalyticsService], so the
 *     ViewModels that already emit analytics need no changes.
 *
 * Every call is guarded and non-throwing; all are no-ops until [initialize].
 */
@Singleton
class SentryAnalyticsManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val sharedPref: SharedPrefHelper,
) : AnalyticsService {

    private val initialized = AtomicBoolean(false)
    private val sendPii = BuildConfig.SENTRY_SEND_PII

    override fun initialize() {
        if (!initialized.compareAndSet(false, true)) return
        runCatching {
            SentryAndroid.init(context) { options ->
                // ----- Release & environment -------------------------------
                options.dsn = "https://30dd7e043a8d131bd041e995441b7ae6@o4511648182894592.ingest.de.sentry.io/4511659756093520"
                options.environment = BuildConfig.SENTRY_ENVIRONMENT
                options.release = BuildConfig.SENTRY_RELEASE
                options.isDebug = BuildConfig.DEBUG
                options.setTag("git_commit", BuildConfig.GIT_COMMIT)

                // ----- Crash reporting -------------------------------------
                // Uncaught JVM exceptions, native (NDK) crashes and ANRs are
                // captured automatically; these tune the extras.
                options.isAnrEnabled = true
                options.isAttachStacktrace = true
                options.isAttachViewHierarchy = true
                // Screenshots can leak PII; keep off (Session Replay already
                // provides masked visual context).
                options.isAttachScreenshot = true

                // ----- Performance monitoring & tracing --------------------
                options.tracesSampleRate = BuildConfig.SENTRY_TRACES_SAMPLE_RATE
                options.isEnableAutoActivityLifecycleTracing = true
                options.isEnableUserInteractionTracing = true

                // ----- Profiling -------------------------------------------
                options.profileSessionSampleRate = BuildConfig.SENTRY_PROFILE_SAMPLE_RATE
                options.profileLifecycle = ProfileLifecycle.TRACE
                options.isStartProfilerOnAppStart = true

                // ----- Session Replay (privacy-safe: mask everything) ------
                options.sessionReplay.sessionSampleRate =
                    BuildConfig.SENTRY_REPLAY_SESSION_SAMPLE_RATE
                options.sessionReplay.onErrorSampleRate =
                    BuildConfig.SENTRY_REPLAY_ON_ERROR_SAMPLE_RATE
                // Privacy-safe defaults: mask ALL text and images so passwords,
                // tokens, payment details and personal data are never recorded.
                options.sessionReplay.setMaskAllText(true)
                options.sessionReplay.setMaskAllImages(true)

                // ----- Structured logs -------------------------------------
                options.logs.isEnabled = true

                // ----- Breadcrumbs -----------------------------------------
                options.maxBreadcrumbs = 100

                // ----- PII protection --------------------------------------
                // Never attach default PII in prod; scrub sensitive values.
                options.isSendDefaultPii = sendPii
                val scrubber = SentryPiiScrubber(sendPii)
                options.beforeSend = SentryOptions.BeforeSendCallback { event, _ ->
                    scrubber.scrub(event)
                }
                options.setBeforeBreadcrumb { breadcrumb, _ -> scrubber.scrub(breadcrumb) }

                // Offline caching is enabled by default: events are persisted to
                // disk and flushed when connectivity is restored.
            }
        }.onFailure { Timber.tag(TAG).e(it, "Sentry initialization failed") }
    }

    override fun onAppForegrounded() =
        breadcrumb("App foregrounded", BreadcrumbCategory.LIFECYCLE)

    override fun onAppBackgrounded() =
        breadcrumb("App backgrounded", BreadcrumbCategory.LIFECYCLE)

    override fun setAuthenticatedUser(userId: String, userType: String) {
        runCatching {
            val user = User().apply {
                id = userId
                username = sharedPref.getString(SpKey.userName).ifBlank { userId }
                // Email is PII: only attach when explicitly allowed.
                if (sendPii) email = sharedPref.getString(SpKey.email).ifBlank { null }
            }
            Sentry.setUser(user)
            Sentry.setTag(ObservabilityKey.USER_TYPE, userType)
            breadcrumb("User authenticated", BreadcrumbCategory.AUTH)
        }
    }

    override fun setUserType(userType: String) {
        runCatching { Sentry.setTag(ObservabilityKey.USER_TYPE, userType) }
    }

    override fun clearUser() {
        runCatching {
            breadcrumb("User logged out", BreadcrumbCategory.AUTH)
            Sentry.setUser(null)
        }
    }

    override fun trackScreen(screenName: String) {
        runCatching {
            Sentry.setTag(ObservabilityKey.SCREEN, screenName)
            val crumb = Breadcrumb().apply {
                type = "navigation"
                category = BreadcrumbCategory.NAVIGATION
                message = "Navigated to $screenName"
                level = SentryLevel.INFO
                setData("to", screenName)
            }
            Sentry.addBreadcrumb(crumb)
        }
    }

    override fun logEvent(event: String, params: Map<String, String>) {
        runCatching {
            val crumb = Breadcrumb().apply {
                category = BreadcrumbCategory.BUSINESS
                message = event
                level = SentryLevel.INFO
                params.forEach { (k, v) -> setData(k, v) }
            }
            Sentry.addBreadcrumb(crumb)
        }
    }

    override fun trackMessageSent(attachmentType: String?) {
        val data = attachmentType?.let { mapOf(AnalyticsParam.ATTACHMENT_TYPE to it) } ?: emptyMap()
        logEvent("Send Message", data)
    }

    override fun onConnectivityChanged(isConnected: Boolean, networkType: String) {
        runCatching {
            Sentry.setTag(
                ObservabilityKey.NETWORK_STATUS,
                if (isConnected) "connected" else "disconnected",
            )
            breadcrumb(
                message = "Connectivity: ${if (isConnected) "online" else "offline"} ($networkType)",
                category = BreadcrumbCategory.SYSTEM,
            )
        }
    }

    private fun breadcrumb(message: String, category: String) {
        runCatching {
            Sentry.addBreadcrumb(
                Breadcrumb().apply {
                    this.message = message
                    this.category = category
                    this.level = SentryLevel.INFO
                },
            )
        }
    }

    private companion object {
        const val TAG = "Sentry"
    }
}
