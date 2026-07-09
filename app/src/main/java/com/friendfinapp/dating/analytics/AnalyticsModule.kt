package com.friendfinapp.dating.analytics

import com.friend.common.analytics.AnalyticsService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Binds the single application-wide [AnalyticsService] implementation.
 *
 * Because the binding lives in the app's [SingletonComponent], the same
 * singleton is injected everywhere (ViewModels in feature modules,
 * infrastructure such as the network layer, and the Application/Activity),
 * while callers only ever depend on the [AnalyticsService] abstraction.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class AnalyticsModule {

    @Binds
    @Singleton
    abstract fun bindAnalyticsService(impl: ClarityAnalyticsManager): AnalyticsService
}
