package com.friendfinapp.dating.analytics

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.core.content.pm.PackageInfoCompat
import com.friend.common.analytics.AnalyticsEvent
import com.friend.common.analytics.AnalyticsParam
import com.friend.common.analytics.AnalyticsPlatform
import com.friend.common.analytics.AnalyticsService
import com.friend.common.analytics.UserType
import com.friend.sharedpref.SharedPrefHelper
import com.microsoft.clarity.Clarity
import com.microsoft.clarity.ClarityConfig
import com.microsoft.clarity.models.LogLevel
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import java.util.Locale
import java.util.UUID
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Microsoft Clarity backed implementation of [AnalyticsService].
 *
 * Responsibilities:
 *  - Initialize Clarity exactly once (guarded by [initialized]).
 *  - Marshal every SDK call to the main thread (Clarity requires it) and never
 *    throw, so analytics can be called from any thread without risk.
 *  - Maintain session-level context (custom tags) and counters (screens
 *    visited, messages sent, session duration).
 *  - Privacy: only non-PII values are ever forwarded. Clarity's on-device
 *    masking (configured on the dashboard) masks all captured text/images by
 *    default; we additionally avoid sending any sensitive fields as tags.
 */
@Singleton
class ClarityAnalyticsManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val sharedPref: SharedPrefHelper,
) : AnalyticsService {

    private val mainHandler = Handler(Looper.getMainLooper())

    private val initialized = AtomicBoolean(false)
    /** Becomes true once Clarity actually starts a session (callback fired). */
    @Volatile
    private var sessionActive = false

    // Session-scoped context we (re)apply whenever a Clarity session starts.
    private val contextTags = mutableMapOf<String, String>()

    // Session counters.
    private val screensVisited = AtomicInteger(0)
    private val messagesSent = AtomicInteger(0)
    @Volatile
    private var sessionStartMs = 0L
    @Volatile
    private var lastScreenName: String? = null

    private val connectivityManager: ConnectivityManager? by lazy {
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    }

    override fun initialize() {
        // Guard against double-initialization from multiple entry points.
        if (!initialized.compareAndSet(false, true)) return

        runOnMain {
            try {
                val config = ClarityConfig(
                    projectId = CLARITY_PROJECT_ID,
                    // Silent by default; flip to LogLevel.Verbose locally to
                    // debug Clarity initialization issues.
                    logLevel = LogLevel.None,
                )
                Clarity.initialize(context.applicationContext, config)

                // Tags/user-id must be (re)applied whenever a session starts so
                // they attach to the correct recording.
                Clarity.setOnSessionStartedCallback { sessionId ->
                    sessionActive = true
                    applyContextTags()
                    Timber.tag(TAG).d("Clarity session started: %s", sessionId)
                }

                seedStaticContext()
                registerConnectivityCallback()
                logAppLaunch()
            } catch (t: Throwable) {
                Timber.tag(TAG).e(t, "Clarity initialization failed")
            }
        }
    }

    override fun onAppForegrounded() {
        sessionStartMs = System.currentTimeMillis()
        screensVisited.set(0)
        messagesSent.set(0)
        logEvent(AnalyticsEvent.SESSION_START)
        logEvent(AnalyticsEvent.APP_FOREGROUNDED)
    }

    override fun onAppBackgrounded() {
        if (sessionStartMs > 0L) {
            val durationSec = (System.currentTimeMillis() - sessionStartMs) / 1000
            setTag(AnalyticsParam.SESSION_DURATION_SEC, durationSec.toString())
        }
        logEvent(AnalyticsEvent.APP_BACKGROUNDED)
        logEvent(AnalyticsEvent.SESSION_END)
    }

    override fun setAuthenticatedUser(userId: String, userType: String) {
        contextTags[AnalyticsParam.USER_ID] = userId
        contextTags[AnalyticsParam.USER_TYPE] = userType
        runOnMain {
            safe { Clarity.setCustomUserId(userId) }
            setTag(AnalyticsParam.USER_ID, userId)
            setTag(AnalyticsParam.USER_TYPE, userType)
        }
    }

    override fun setUserType(userType: String) {
        contextTags[AnalyticsParam.USER_TYPE] = userType
        setTag(AnalyticsParam.USER_TYPE, userType)
    }

    override fun clearUser() {
        contextTags.remove(AnalyticsParam.USER_ID)
        contextTags[AnalyticsParam.USER_TYPE] = UserType.ANONYMOUS
        setTag(AnalyticsParam.USER_TYPE, UserType.ANONYMOUS)
    }

    override fun trackScreen(screenName: String) {
        // De-duplicate: ignore repeats of the same screen (config changes /
        // recompositions won't emit duplicate events).
        if (screenName == lastScreenName) return
        lastScreenName = screenName

        val count = screensVisited.incrementAndGet()
        runOnMain {
            safe { Clarity.setCurrentScreenName(screenName) }
            setTag(AnalyticsParam.SCREENS_VISITED, count.toString())
        }
        logEvent(AnalyticsEvent.SCREEN_VIEW, mapOf(AnalyticsParam.SCREEN_NAME to screenName))
    }

    override fun logEvent(event: String, params: Map<String, String>) {
        runOnMain {
            safe { Clarity.sendCustomEvent(event) }
            // Clarity has no per-event parameters; expose params as filterable
            // session tags instead.
            params.forEach { (key, value) -> setTag(key, value) }
        }
    }

    override fun trackMessageSent(attachmentType: String?) {
        val count = messagesSent.incrementAndGet()
        setTag(AnalyticsParam.MESSAGES_SENT, count.toString())
        logEvent(AnalyticsEvent.SEND_MESSAGE)
        if (attachmentType != null) {
            logEvent(
                AnalyticsEvent.MESSAGE_ATTACHMENT_SENT,
                mapOf(AnalyticsParam.ATTACHMENT_TYPE to attachmentType),
            )
        }
    }

    override fun onConnectivityChanged(isConnected: Boolean, networkType: String) {
        logEvent(
            AnalyticsEvent.NETWORK_CONNECTIVITY_CHANGED,
            mapOf(
                AnalyticsParam.IS_CONNECTED to isConnected.toString(),
                AnalyticsParam.NETWORK_TYPE to networkType,
            ),
        )
    }

    // ----- internals -------------------------------------------------------

    /** Static (device/app) context that is stable for the whole process. */
    private fun seedStaticContext() {
        contextTags[AnalyticsParam.PLATFORM] = AnalyticsPlatform.ANDROID
        contextTags[AnalyticsParam.APP_VERSION] = appVersionName()
        contextTags[AnalyticsParam.BUILD_NUMBER] = appVersionCode()
        contextTags[AnalyticsParam.OS_VERSION] = Build.VERSION.RELEASE ?: "unknown"
        contextTags[AnalyticsParam.DEVICE_LANGUAGE] = Locale.getDefault().language
        contextTags[AnalyticsParam.ANONYMOUS_USER_ID] = anonymousId()
        if (!contextTags.containsKey(AnalyticsParam.USER_TYPE)) {
            contextTags[AnalyticsParam.USER_TYPE] = UserType.ANONYMOUS
        }
    }

    /** Re-apply all known context tags to the freshly started session. */
    private fun applyContextTags() {
        contextTags.forEach { (key, value) -> setTag(key, value) }
        contextTags[AnalyticsParam.USER_ID]?.let { safe { Clarity.setCustomUserId(it) } }
    }

    private fun logAppLaunch() {
        logEvent(AnalyticsEvent.APP_LAUNCH)
        if (!sharedPref.getBoolean(KEY_FIRST_OPEN_DONE)) {
            logEvent(AnalyticsEvent.FIRST_APP_OPEN)
            sharedPref.putBool(KEY_FIRST_OPEN_DONE, true)
        }
    }

    private fun anonymousId(): String {
        val existing = sharedPref.getString(KEY_ANON_ID)
        if (existing.isNotEmpty()) return existing
        val generated = UUID.randomUUID().toString()
        sharedPref.putString(KEY_ANON_ID, generated)
        return generated
    }

    private fun appVersionName(): String = try {
        context.packageManager.getPackageInfo(context.packageName, 0).versionName ?: "unknown"
    } catch (t: Throwable) {
        "unknown"
    }

    private fun appVersionCode(): String = try {
        val info = context.packageManager.getPackageInfo(context.packageName, 0)
        PackageInfoCompat.getLongVersionCode(info).toString()
    } catch (t: Throwable) {
        "unknown"
    }

    private fun registerConnectivityCallback() {
        val cm = connectivityManager ?: return
        try {
            cm.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    val type = cm.getNetworkCapabilities(network).toNetworkType()
                    onConnectivityChanged(isConnected = true, networkType = type)
                }

                override fun onLost(network: Network) {
                    onConnectivityChanged(isConnected = false, networkType = "None")
                }
            })
        } catch (t: Throwable) {
            Timber.tag(TAG).w(t, "Unable to register connectivity callback")
        }
    }

    private fun NetworkCapabilities?.toNetworkType(): String = when {
        this == null -> "Unknown"
        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "WiFi"
        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "Cellular"
        hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> "Ethernet"
        else -> "Other"
    }

    /** Set a Clarity custom tag; only meaningful once a session is active. */
    private fun setTag(key: String, value: String) {
        if (value.isBlank()) return
        runOnMain { safe { Clarity.setCustomTag(key, value) } }
    }

    private inline fun safe(block: () -> Unit) {
        if (!initialized.get()) return
        try {
            block()
        } catch (t: Throwable) {
            Timber.tag(TAG).w(t, "Clarity call failed")
        }
    }

    private fun runOnMain(block: () -> Unit) {
        if (Looper.myLooper() == Looper.getMainLooper()) block()
        else mainHandler.post(block)
    }

    companion object {
        private const val TAG = "Analytics"
        private const val CLARITY_PROJECT_ID = "xjodiqruhi"
        private const val KEY_ANON_ID = "analytics_anonymous_id"
        private const val KEY_FIRST_OPEN_DONE = "analytics_first_open_done"
    }
}
