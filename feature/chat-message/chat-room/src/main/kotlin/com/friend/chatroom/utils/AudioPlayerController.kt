package com.friend.chatroom.utils

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

data class AudioUiState(
    val activeId: String? = null,
    val isPlaying: Boolean = false,
    val positionMs: Long = 0L,
)

class AudioPlayerController(context: Context) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private var tickerJob: Job? = null

    private val player = ExoPlayer.Builder(context).build()

    private val _state = MutableStateFlow(AudioUiState())
    val state: StateFlow<AudioUiState> = _state.asStateFlow()

    private val listener = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            _state.update { it.copy(isPlaying = isPlaying) }
            if (isPlaying) startTicker() else stopTicker()
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            if (playbackState == Player.STATE_ENDED) {
                player.seekTo(0)
                player.pause()
                _state.update { it.copy(isPlaying = false, positionMs = 0L) }
            }
        }
    }

    init {
        player.addListener(listener)
    }

    fun toggle(id: String, url: String) {
        val s = _state.value
        if (s.activeId == id && player.isPlaying) {
            pause()
        } else {
            play(id, url)
        }
    }

    fun play(id: String, url: String) {
        val switching = _state.value.activeId != id

        if (switching) {
            // this stops whatever was playing before (ONE player)
            player.stop()
            player.setMediaItem(MediaItem.fromUri(url))
            player.prepare()
            player.seekTo(0)
            _state.update { it.copy(activeId = id, positionMs = 0L, isPlaying = false) }
        } else {
            // same item: if ended/near-end, rewind
            val dur = player.duration
            val atEnd = dur > 0 && player.currentPosition >= dur - 200
            if (player.playbackState == Player.STATE_ENDED || atEnd) player.seekTo(0)
        }

        player.play()
    }

    fun pause() {
        player.pause()
    }

    fun seekTo(id: String, positionMs: Long) {
        if (_state.value.activeId == id) {
            player.seekTo(positionMs.coerceAtLeast(0L))
            _state.update { it.copy(positionMs = player.currentPosition.coerceAtLeast(0L)) }
        }
    }

    private fun startTicker() {
        if (tickerJob?.isActive == true) return
        tickerJob = scope.launch {
            while (isActive && player.isPlaying) {
                _state.update { it.copy(positionMs = player.currentPosition.coerceAtLeast(0L)) }
                delay(250L)
            }
        }
    }

    private fun stopTicker() {
        tickerJob?.cancel()
        tickerJob = null
    }

    fun release() {
        stopTicker()
        player.removeListener(listener)
        player.release()
        scope.cancel()
    }
}

@Composable
fun rememberAudioPlayerController(): AudioPlayerController {
    val context = LocalContext.current.applicationContext
    val lifecycleOwner = LocalLifecycleOwner.current

    val controller = remember { AudioPlayerController(context) }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) controller.pause()
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            controller.release()
        }
    }

    return controller
}


