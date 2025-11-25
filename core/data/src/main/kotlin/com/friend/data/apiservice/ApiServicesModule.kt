package com.friend.data.apiservice

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
    ): CredentialApiServices {
        return retrofit.create(CredentialApiServices::class.java)
    }

    @Provides
    @Singleton
    fun provideSearchApiService(
        @AppBaseUrl retrofit: Retrofit,
    ): SearchApiServices {
        return retrofit.create(SearchApiServices::class.java)
    }

    @Provides
    @Singleton
    fun provideProfileManagerApiService(
        @AppBaseUrl retrofit: Retrofit,
    ): ProfileManagerApiServices {
        return retrofit.create(ProfileManagerApiServices::class.java)
    }

    @Provides
    @Singleton
    fun provideChatMessagesApiService(
        @AppBaseUrl retrofit: Retrofit,
    ): ChatMessageApiServices {
        return retrofit.create(ChatMessageApiServices::class.java)
    }
}