package com.friend.data.apiservice

import com.friend.di.authrefresh.AuthRefreshApiService
import com.friend.di.authrefresh.AuthRefreshServiceHolder
import com.friend.di.qualifier.AppBaseUrl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton
import kotlin.jvm.java

@Module
@InstallIn(SingletonComponent::class)
object ApiServicesModule {

    @Provides
    @Singleton
    fun provideCredentialApiService(
        @AppBaseUrl retrofit: Retrofit,
        authRefreshServiceHolder: AuthRefreshServiceHolder,
    ): AuthApiServices {
        authRefreshServiceHolder.setAuthRefreshApi(retrofit.create(AuthRefreshApiService::class.java))
        return retrofit.create(AuthApiServices::class.java)
    }

    @Provides
    @Singleton
    fun provideSearchApiService(
        @AppBaseUrl retrofit: Retrofit,
        authRefreshServiceHolder: AuthRefreshServiceHolder,
    ): SearchApiServices {
        authRefreshServiceHolder.setAuthRefreshApi(retrofit.create(AuthRefreshApiService::class.java))
        return retrofit.create(SearchApiServices::class.java)
    }

    @Provides
    @Singleton
    fun provideProfileManagerApiService(
        @AppBaseUrl retrofit: Retrofit,
        authRefreshServiceHolder: AuthRefreshServiceHolder,
    ): ProfileManagerApiServices {
        authRefreshServiceHolder.setAuthRefreshApi(retrofit.create(AuthRefreshApiService::class.java))
        return retrofit.create(ProfileManagerApiServices::class.java)
    }

    @Provides
    @Singleton
    fun provideChatMessagesApiService(
        @AppBaseUrl retrofit: Retrofit,
        authRefreshServiceHolder: AuthRefreshServiceHolder,
    ): ChatMessageApiServices {
        authRefreshServiceHolder.setAuthRefreshApi(retrofit.create(AuthRefreshApiService::class.java))
        return retrofit.create(ChatMessageApiServices::class.java)
    }
}