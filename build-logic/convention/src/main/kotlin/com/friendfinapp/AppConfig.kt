package com.friendfinapp

import org.gradle.api.JavaVersion

object AppConfig {
    const val applicationId = "com.friendfinapp.dating"
    const val minimumSdkVersion = 24
    const val compileSdkVersion = 37
    const val targetSdkVersion = 37
    var testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    val compatibilityVersion = JavaVersion.VERSION_17
    const val versionCode = 173
    const val versionName = "1.1.172"
}