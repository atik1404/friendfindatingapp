package com.friendfinapp.dating

import android.app.Activity
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentResolver
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.net.toUri
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
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
    Application.ActivityLifecycleCallbacks, DefaultLifecycleObserver {

    @Inject
    lateinit var sharedPrefHelper: SharedPrefHelper

    // TEST App Open Ad Unit ID (replace with your real one before release)
    // Dedicated test ID from Google docs:
    private val APP_OPEN_TEST_AD_UNIT_ID = "ca-app-pub-3940256099942544/9257395921"

    private lateinit var appOpenAdManager: AppOpenAdManager
    private var currentActivity: Activity? = null

    override fun onCreate() {
        super<Application>.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())

        setupNotificationChannels()
        getFirebaseToken()
//
//        MobileAds.initialize(this)
//
//        registerActivityLifecycleCallbacks(this)
//        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
//
//        appOpenAdManager = AppOpenAdManager(APP_OPEN_TEST_AD_UNIT_ID)
//        appOpenAdManager.loadAd(this)
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        currentActivity?.let { activity ->
            appOpenAdManager.showAdIfAvailable(activity)
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
        isEnableVibration: Boolean = true
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
}

object KeyFrame {
    const val FOREGROUND_NOTIFICATION_CHANNEL_ID = "FCM Notification"
}