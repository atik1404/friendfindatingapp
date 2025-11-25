package com.friendfinapp.dating.di

import android.content.Context
import com.friend.sharedpref.SharedPrefHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    fun sharePrefHelper(@ApplicationContext context: Context): SharedPrefHelper =
        SharedPrefHelper(context)

//    @Provides
//    @Singleton
//    @AppBuildType
//    fun provideBuildType() = Utils.getBuildTypeName(BuildConfig.BUILD_TYPE)
//
//    @Provides
//    @Singleton
//    @AppVersion
//    fun provideVersion() = BuildConfig.VERSION_NAME
}