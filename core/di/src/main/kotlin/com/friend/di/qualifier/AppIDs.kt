package com.friend.di.qualifier

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class GoogleWebClientId

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class AppOpenAdId

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class BannerAdId
