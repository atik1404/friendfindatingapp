package com.friend.data.apiservice


import com.friend.di.authrefresh.AuthRefreshApiService
import com.friend.di.authrefresh.AuthRefreshServiceHolder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiServicesModule {
    @Provides
    @Singleton
    fun provideCredentialApiService(
        @CredentialBaseUrl retrofit: Retrofit,
        authRefreshServiceHolder: AuthRefreshServiceHolder
    ): DemoApiServices {
        authRefreshServiceHolder.setAuthRefreshApi(retrofit.create(AuthRefreshApiService::class.java))
        return retrofit.create(DemoApiServices::class.java)
    }
}