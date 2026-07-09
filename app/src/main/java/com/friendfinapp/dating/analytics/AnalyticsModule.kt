package com.friendfinapp.dating.analytics

import com.friend.common.analytics.AnalyticsService
import com.friend.common.observability.MonitoringService
import com.friendfinapp.dating.observability.SentryAnalyticsManager
import com.friendfinapp.dating.observability.SentryMonitoringService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Provides the application-wide observability bindings.
 *
 * - [AnalyticsService] is a [CompositeAnalyticsService] that fans out to both
 *   Clarity and Sentry. Because it lives in the [SingletonComponent], the same
 *   singleton is injected everywhere (feature ViewModels, the network layer,
 *   the Application/Activity) while callers depend only on the abstraction.
 *   Sentry is listed first so it is initialized as early as possible to capture
 *   startup issues.
 * - [MonitoringService] exposes Sentry's reliability/performance APIs
 *   (exceptions, logs, spans).
 */
@Module
@InstallIn(SingletonComponent::class)
object AnalyticsModule {

    @Provides
    @Singleton
    fun provideAnalyticsService(
        sentry: SentryAnalyticsManager,
        clarity: ClarityAnalyticsManager,
    ): AnalyticsService = CompositeAnalyticsService(listOf(sentry, clarity))

    @Provides
    @Singleton
    fun provideMonitoringService(impl: SentryMonitoringService): MonitoringService = impl
}
