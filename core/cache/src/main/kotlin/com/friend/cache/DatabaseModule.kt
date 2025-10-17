package com.friend.cache

import android.app.Application
import androidx.room.Room
import com.friend.cache.dao.DemoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(
            application, AppDatabase::class.java,
            "friendfin.db"
        )
            .build()
    }

    @Singleton
    @Provides
    fun provideSearchCitiesDao(database: AppDatabase): DemoDao =
        database.demoDao()
}
