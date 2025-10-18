package com.friendfinapp.dating.navigation

import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.firstOrNull

// NavResultManager.kt
class NavResultManager {
    private val bus = mutableMapOf<String, MutableSharedFlow<Any>>()

    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> flow(key: String): MutableSharedFlow<T> =
        bus.getOrPut(key) {
            // Keep the last value so a *future* subscriber (Main after pop) can read it.
            MutableSharedFlow<Any>(replay = 1, extraBufferCapacity = 0)
        } as MutableSharedFlow<T>

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> results(key: String): Flow<T> = flow<T>(key)

    // IMPORTANT: do NOT remove() here
    fun <T : Any> trySet(key: String, value: T) {
        flow<T>(key).tryEmit(value)
    }

    // OPTIONAL: if you ever use a suspending set
    suspend fun <T : Any> set(key: String, value: T) {
        flow<T>(key).emit(value)
    }

    // Consume once and then clean up the channel
    suspend fun <T : Any> takeOnceOrNull(key: String): T? {
        val v = results<T>(key).firstOrNull()
        if (v != null) bus.remove(key)
        return v
    }

    fun cancel(key: String) {
        bus.remove(key)
    }
}

val LocalNavResultManager = staticCompositionLocalOf { NavResultManager() }
