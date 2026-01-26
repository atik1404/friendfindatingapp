package com.friendfinapp.dating

import com.friend.di.qualifier.AppBaseUrl
import com.friend.di.qualifier.AppFileBaseUrl
import com.friend.di.qualifier.AppOpenAdId
import com.friend.di.qualifier.BannerAdId
import com.friend.di.qualifier.GoogleWebClientId
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class BaseUrlModule {
    @Provides
    @AppBaseUrl
    fun provideBaseUrl(): String = "https://friendfin.com/friendfinapi/"

    @Provides
    @AppFileBaseUrl
    fun provideFileBaseUrl(): String = "https://friendfin.com/friendfinapi/"

    @Provides
    @GoogleWebClientId
    fun provideGoogleWebClientId(): String =
        "176628957073-lll0macf6raivbvguuic9m9aoiuop4tn.apps.googleusercontent.com"

    @Provides
    @AppOpenAdId
    fun provideAppOpenAdId(): String = "ca-app-pub-3432824199077306/6994471618"

    @Provides
    @BannerAdId
    fun provideBannerAdId(): String = "ca-app-pub-3432824199077306/3324531097"
}



