package com.friend.videoplayer

import android.app.Application
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.friend.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoPlayerViewModel @Inject constructor(app: Application) : BaseViewModel() {

    private val _uiState = MutableStateFlow(VideoUiState())
    val uiState: StateFlow<VideoUiState> = _uiState

    val player: ExoPlayer = ExoPlayer.Builder(app.applicationContext).build().apply {
        repeatMode = Player.REPEAT_MODE_OFF
        playWhenReady = true

        addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _uiState.update { it.copy(isPlaying = isPlaying) }
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                val isBuffering = playbackState == Player.STATE_BUFFERING
                val isReady = playbackState == Player.STATE_READY
                _uiState.update { state ->
                    state.copy(
                        isBuffering = isBuffering,
                        isReady = isReady,
                        durationMs = duration.coerceAtLeast(0L)
                    )
                }
            }

            override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
                _uiState.update { it.copy(error = error.message ?: "Playback error") }
            }
        })
    }

    private var progressJob: Job? = null

    fun setUrl(url: String) {
        if (_uiState.value.url == url) return

        _uiState.update { it.copy(url = url, error = null, positionMs = 0L, durationMs = 0L) }

        player.setMediaItem(MediaItem.fromUri(url))
        player.prepare()
        player.play()

        startProgressUpdates()
    }

    fun play() = player.play()
    fun pause() = player.pause()

    fun togglePlayPause() {
        if (player.isPlaying) pause() else play()
    }

    fun seekTo(positionMs: Long) {
        val safe = positionMs.coerceIn(0L, (player.duration.takeIf { it > 0 } ?: Long.MAX_VALUE))
        player.seekTo(safe)
        _uiState.update { it.copy(positionMs = safe) }
    }

    fun seekBy(deltaMs: Long) {
        seekTo(player.currentPosition + deltaMs)
    }

    fun onStop() {
        // Common UX: pause when leaving screen/background
        pause()
    }

    fun onStart() {
        // Optional: resume if you want
        // play()
        startProgressUpdates()
    }

    private fun startProgressUpdates() {
        if (progressJob?.isActive == true) return
        progressJob = viewModelScope.launch {
            while (isActive) {
                val duration = player.duration.coerceAtLeast(0L)
                val position = player.currentPosition.coerceAtLeast(0L)
                val buffered = player.bufferedPercentage.coerceIn(0, 100)

                _uiState.update {
                    it.copy(
                        durationMs = duration,
                        positionMs = position.coerceAtMost(duration.takeIf { d -> d > 0 }
                            ?: position),
                        bufferedPercent = buffered
                    )
                }
                delay(400L)
            }
        }
    }

    override fun onCleared() {
        progressJob?.cancel()
        player.release()
        super.onCleared()
    }
}