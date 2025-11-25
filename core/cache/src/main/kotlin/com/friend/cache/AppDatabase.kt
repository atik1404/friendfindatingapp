package com.friend.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.friend.cache.dao.DemoDao
import com.friend.entity.search.DemoApiEntity

@Database(
    entities =
        [
            DemoApiEntity::class,
        ],
    version = 2, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun demoDao(): DemoDao
}