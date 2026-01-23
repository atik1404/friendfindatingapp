package com.friend.videoplayer

data class VideoUiState(
    val url: String? = null,
    val isReady: Boolean = false,
    val isPlaying: Boolean = false,
    val isBuffering: Boolean = false,
    val durationMs: Long = 0L,
    val positionMs: Long = 0L,
    val bufferedPercent: Int = 0,
    val error: String? = null
)