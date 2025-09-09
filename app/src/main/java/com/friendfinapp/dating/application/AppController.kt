package com.friendfinapp.dating.application

import android.app.Application
import android.content.res.Resources
import com.friendfinapp.dating.appopen.AppOpenManager

import com.google.android.gms.ads.MobileAds

import com.google.android.gms.ads.RequestConfiguration

class AppController : Application() {

    var appOpenManager: AppOpenManager? = null
    override fun onCreate() {
        super.onCreate()


        instance = this
        res = resources

        MobileAds.initialize(
            this
        ) { }

        val testDeviceIds = listOf("33BE2250B43518CCDA7DE426D04EE231")
        val configuration = RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build()
        MobileAds.setRequestConfiguration(configuration)
        appOpenManager = AppOpenManager(this)


    }

    companion object {
        @get:Synchronized
        lateinit var instance: AppController
            private set
        lateinit var res: Resources
    }
}