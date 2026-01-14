package com.friendfinapp.dating

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import com.google.android.gms.ads.*
import com.google.android.gms.ads.appopen.AppOpenAd
import timber.log.Timber
import java.lang.ref.WeakReference
import java.util.Date
import java.util.concurrent.TimeUnit

class AppOpenAdManager(
    private val adUnitId: String
) {
    private var appOpenAd: AppOpenAd? = null
    private var isLoadingAd: Boolean = false
    var isShowingAd: Boolean = false
        private set

    private var loadTime: Long = 0L
    
    private var pendingShow: Boolean = false
    private var lastActivityRef: WeakReference<Activity>? = null

    fun loadAd(context: Context) {
        if (isLoadingAd || isAdAvailable()) return

        isLoadingAd = true
        val request = AdRequest.Builder().build()

        AppOpenAd.load(
            context,
            adUnitId,
            request,
            object : AppOpenAd.AppOpenAdLoadCallback() {

                override fun onAdLoaded(ad: AppOpenAd) {
                    appOpenAd = ad
                    isLoadingAd = false
                    loadTime = Date().time
                    if (pendingShow && !isShowingAd) {
                        pendingShow = false
                        lastActivityRef?.get()?.let { activity ->
                            if (!activity.isFinishing && !activity.isDestroyed) {
                                Handler(Looper.getMainLooper()).post {
                                    showAdIfAvailable(activity)
                                }
                            }
                        }
                    }
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    isLoadingAd = false
                    pendingShow = false
                    Timber.e("Failed to load: ${loadAdError.message}")
                }
            }
        )
    }

    fun showAdIfAvailable(
        activity: Activity,
        onComplete: () -> Unit = {}
    ) {
        lastActivityRef = WeakReference(activity)

        if (isShowingAd) return

        if (!isAdAvailable()) {
            pendingShow = true
            loadAd(activity)
            return
        }

        isShowingAd = true
        pendingShow = false

        appOpenAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdShowedFullScreenContent() {
                Timber.e("App open ad showed")
            }

            override fun onAdDismissedFullScreenContent() {
                appOpenAd = null
                isShowingAd = false
                onComplete()
                loadAd(activity)
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                appOpenAd = null
                isShowingAd = false
                onComplete()
                loadAd(activity)
            }
        }

        appOpenAd?.show(activity)
    }

    private fun isAdAvailable(): Boolean {
        return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4)
    }

    private fun wasLoadTimeLessThanNHoursAgo(hours: Long): Boolean {
        val ageHours = TimeUnit.MILLISECONDS.toHours(Date().time - loadTime)
        return ageHours < hours
    }
}
