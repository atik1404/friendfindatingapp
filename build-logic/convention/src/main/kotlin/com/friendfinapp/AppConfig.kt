package com.friendfinapp

import org.gradle.api.JavaVersion

object AppConfig {
    const val applicationId = "com.friendfinapp.dating"
    const val minimumSdkVersion = 23
    const val compileSdkVersion = 36
    const val targetSdkVersion = 36
    var testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    val compatibilityVersion = JavaVersion.VERSION_11
    const val versionCode  = 145
    const val versionName  = "1.0.144"
}