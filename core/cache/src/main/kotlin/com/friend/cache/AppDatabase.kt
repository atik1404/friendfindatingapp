package com.friend.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.friend.cache.dao.DemoDao

@Database(
    entities =
        [
            StoppageRoomEntity::class,
            CityRoomEntity::class,
            TicketFormatRoomEntity::class
        ],
    version = 2, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun demoDao(): DemoDao
}