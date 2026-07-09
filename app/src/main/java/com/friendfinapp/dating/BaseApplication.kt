package com.friendfinapp.dating

import android.app.Activity
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryPurchasesParams
import com.friend.common.analytics.AnalyticsService
import com.friend.common.constant.AppConstants
import com.friend.di.qualifier.AppOpenAdId
import com.friend.sharedpref.SharedPrefHelper
import com.friend.sharedpref.SpKey
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject
import com.friend.designsystem.R as Res

@HiltAndroidApp
class BaseApplication : Application(),
    Application.ActivityLifecycleCallbacks, DefaultLifecycleObserver, PurchasesUpdatedListener {
    @Inject
    lateinit var sharedPrefHelper: SharedPrefHelper

    @Inject
    lateinit var analytics: AnalyticsService

    private var lastBackgroundTime: Long = 0L

    /**
     * Dedicated, always-registered observer for analytics session lifecycle.
     * Kept separate from the ads observer (which only runs in release) so
     * foreground/background + session events are reported in every build.
     */
    private val analyticsLifecycleObserver = object : DefaultLifecycleObserver {
        override fun onStart(owner: LifecycleOwner) {
            analytics.onAppForegrounded()
        }

        override fun onStop(owner: LifecycleOwner) {
            analytics.onAppBackgrounded()
        }
    }

    @Inject
    @AppOpenAdId
    lateinit var appOpenAdId: String

    private lateinit var appOpenAdManager: AppOpenAdManager
    private var currentActivity: Activity? = null

    private val billingClient: BillingClient by lazy {
        BillingClient.newBuilder(this)
            .setListener { _, _ ->
                // Handle purchase updates here
            }
            .enablePendingPurchases(
                PendingPurchasesParams.newBuilder()
                    .enableOneTimeProducts()
                    .build()
            )
            .build()
    }

    override fun onCreate() {
        super<Application>.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())

        appOpenAdManager = AppOpenAdManager(appOpenAdId)

        // Initialize analytics once, at process startup, then observe the
        // process lifecycle for session start/end + foreground/background.
        analytics.initialize()
        ProcessLifecycleOwner.get().lifecycle.addObserver(analyticsLifecycleObserver)

        if (!BuildConfig.DEBUG) {
            MobileAds.initialize(this)

            registerActivityLifecycleCallbacks(this)
            ProcessLifecycleOwner.get().lifecycle.addObserver(this)
            startConnection()
        }

        setupNotificationChannels()
        getFirebaseToken()
    }

    override fun onStop(owner: LifecycleOwner) {
        lastBackgroundTime = System.currentTimeMillis()
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        val elapsed = System.currentTimeMillis() - lastBackgroundTime
        if (!AppConstants.isPremiumUser && (lastBackgroundTime == 0L || elapsed > 60_000)) {
            currentActivity?.let { activity ->
                appOpenAdManager.showAdIfAvailable(activity)
            }
        }
    }

    // Track current activity (don’t overwrite while an ad is showing)
    override fun onActivityStarted(activity: Activity) {
        if (!appOpenAdManager.isShowingAd) {
            currentActivity = activity
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityResumed(activity: Activity) {}
    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {}

    private fun setupNotificationChannels() {
        /** Foreground channel
         *  Used for ongoing notifications when the app is running in the foreground **/
        createNotificationChannel(
            KeyFrame.FOREGROUND_NOTIFICATION_CHANNEL_ID,
            KeyFrame.FOREGROUND_NOTIFICATION_CHANNEL_ID
        )

        /** Default channel
         *  Used for standard notifications with default vibration pattern (0ms delay, 1000ms vibrate) **/
        createNotificationChannel(
            getString(Res.string.default_notification_id),
            getString(Res.string.default_notification_channel_name),
            vibrationPattern = longArrayOf(0, 1000)
        )

        /** Custom channels
         *  Various combinations of sound and vibration settings for different notification types **/
        createNotificationChannel(
            getString(Res.string.custom_sound_on_vibration_on_notification_id),
            getString(Res.string.custom_sound_notification_channel_name),
            vibrationPattern = longArrayOf(0, 2000)
        )

        /** Channel with sound enabled but vibration disabled **/
        createNotificationChannel(
            getString(Res.string.custom_sound_on_vibration_off_notification_id),
            getString(Res.string.custom_sound_notification_channel_name),
            vibrationPattern = longArrayOf(0),
            isEnableVibration = false
        )

        /** Channel with vibration enabled but no sound **/
        createNotificationChannel(
            getString(Res.string.custom_sound_off_vibration_on_notification_id),
            getString(Res.string.custom_sound_notification_channel_name),
            vibrationPattern = longArrayOf(0, 2000)
        )

        /** Channel with both sound and vibration disabled **/
        createNotificationChannel(
            getString(Res.string.custom_sound_off_vibration_off_notification_id),
            getString(Res.string.custom_sound_notification_channel_name),
            vibrationPattern = longArrayOf(0),
            isEnableVibration = false
        )
    }

    private fun createNotificationChannel(
        channelId: String,
        channelName: String,
        soundUri: Uri? = null,
        vibrationPattern: LongArray? = null,
        isEnableVibration: Boolean = true,
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                enableLights(true)
                enableVibration(isEnableVibration)
                vibrationPattern?.let { this.vibrationPattern = it }

                soundUri?.let { uri ->
                    val attributes = AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .build()
                    setSound(uri, attributes)
                }
            }

            getSystemService(NotificationManager::class.java)?.createNotificationChannel(
                notificationChannel
            )
        }
    }

    private fun getFirebaseToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            Timber.e("fcmToken: ${task.result}")
            sharedPrefHelper.putString(SpKey.fcmToken, task.result ?: "")
        })
    }

    private fun startConnection() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    checkActivePurchases(billingClient) { isPremium ->
                        if (!isPremium) {
                            appOpenAdManager.loadAd(this@BaseApplication)
                        }
                    }
                }
            }

            override fun onBillingServiceDisconnected() {
            }
        })
    }

    fun checkActivePurchases(billingClient: BillingClient, onResult: (Boolean) -> Unit) {
        if (!billingClient.isReady) {
            onResult(false)
            return
        }

        val params = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.SUBS)
            .build()

        billingClient.queryPurchasesAsync(params) { billingResult, purchases ->
            val hasActivePurchase =
                billingResult.responseCode == BillingClient.BillingResponseCode.OK &&
                        purchases.any { it.purchaseState == Purchase.PurchaseState.PURCHASED }

            AppConstants.isPremiumUser = hasActivePurchase
            onResult(hasActivePurchase)
        }
    }

    override fun onPurchasesUpdated(
        p0: BillingResult,
        p1: List<Purchase?>?,
    ) {
    }
}

object KeyFrame {
    const val FOREGROUND_NOTIFICATION_CHANNEL_ID = "FCM Notification"
}