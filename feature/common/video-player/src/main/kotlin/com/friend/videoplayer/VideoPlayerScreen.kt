package com.friend.videoplayer

import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.friend.designsystem.spacing.IconSizeToken
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPadding
import com.friend.designsystem.theme.surfaceColors
import com.friend.ui.common.AppToolbar
import com.friend.ui.components.AppIconButton
import com.friend.ui.components.AppScaffold
import com.friend.ui.components.AppText
import com.friend.ui.preview.LightPreview
import kotlinx.coroutines.delay
import com.friend.designsystem.R as Res

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoPlayerScreen(
    state: VideoUiState,
    exoPlayer: ExoPlayer,
    onClose: () -> Unit,
    onStart: () -> Unit,
    onStop: () -> Unit,
    onSeek: (Long) -> Unit, // Renamed from onForward for clarity
    onTogglePlayPause: () -> Unit,
    onForward3s: () -> Unit,
    onBackward3s: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> onStart.invoke()
                Lifecycle.Event.ON_STOP -> onStop.invoke()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    var isControlRowVisible by remember { mutableStateOf(false) }
    LaunchedEffect(isControlRowVisible) {
        if (isControlRowVisible) {
            delay(2000L)
            isControlRowVisible = false
        }
    }

    AppScaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        isAdsVisible = false,
        topBar = {
            AppToolbar(
                title = stringResource(Res.string.title_video),
                onBackClick = onClose
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    MaterialTheme.colorScheme.surfaceVariant,
                    RoundedCornerShape(14.dp)
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    isControlRowVisible = !isControlRowVisible
                },
            contentAlignment = Alignment.Center
        ) {
            AndroidView(
                modifier = Modifier
                    .fillMaxSize(),
                factory = { ctx ->
                    PlayerView(ctx).apply {
                        layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                        useController = false
                        player = exoPlayer
                    }
                },
                update = { it.player = exoPlayer }
            )

            if (state.isBuffering) {
                CircularProgressIndicator()
            }

            if (isControlRowVisible)
                ButtonsUi(
                    isPlaying = state.isPlaying,
                    onTogglePlayPause = onTogglePlayPause,
                    onForward3s = onForward3s,
                    onBackward3s = onBackward3s
                )

            val duration = state.durationMs.takeIf { it > 0L } ?: 0L
            val position = state.positionMs.coerceIn(0L, duration)

            Column(
                modifier = Modifier
                    .appPadding(SpacingToken.medium)
                    .align(Alignment.BottomCenter),
                verticalArrangement = Arrangement.spacedBy(SpacingToken.tiny)
            ) {
                Slider(
                    value = if (duration > 0) position.toFloat() else 0f,
                    valueRange = 0f..(duration.toFloat().coerceAtLeast(1f)),
                    onValueChange = { onSeek(it.toLong()) }
                )

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    AppText(text = formatMs(position), fontWeight = FontWeight.Light)
                    AppText(text = formatMs(duration), fontWeight = FontWeight.Light)
                }
            }

            state.error?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun ButtonsUi(
    isPlaying: Boolean,
    onTogglePlayPause: () -> Unit,
    onForward3s: () -> Unit,
    onBackward3s: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Backward 3s
        AppIconButton(
            iconSize = IconSizeToken.medium,
            modifier = Modifier
                .background(
                    color = MaterialTheme.surfaceColors.primary.copy(alpha = .5f),
                    shape = CircleShape
                ),
            vectorIcon = Icons.Default.SkipPrevious,
            onClick = onBackward3s,
            tint = MaterialTheme.surfaceColors.white
        )

        Spacer(Modifier.width(SpacingToken.tiny))
        // Play/Pause
        AppIconButton(
            iconSize = IconSizeToken.medium,
            onClick = onTogglePlayPause,
            modifier = Modifier.background(
                color = MaterialTheme.surfaceColors.primary.copy(alpha = .5f),
                shape = CircleShape
            ),
            vectorIcon = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
            tint = MaterialTheme.surfaceColors.white
        )
        Spacer(Modifier.width(SpacingToken.tiny))
        // Forward 3s
        AppIconButton(
            iconSize = IconSizeToken.medium,
            modifier = Modifier
                .background(
                    color = MaterialTheme.surfaceColors.primary.copy(alpha = .5f),
                    shape = CircleShape
                ),
            onClick = onForward3s,
            vectorIcon = Icons.Default.SkipNext,
            tint = MaterialTheme.surfaceColors.white
        )
    }
}

private fun formatMs(ms: Long): String {
    val totalSeconds = (ms / 1000).coerceAtLeast(0)
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%d:%02d".format(minutes, seconds)
}

@Composable
@LightPreview
private fun ScreenPreview() {
//    VideoPlayerScreen(
//        state = VideoUiState(),
//        exoPlayer = ExoPlayer.Builder(LocalContext.current).build(),
//        onClose = {},
//        onStart = {},
//        onStop = {},
//        onSeek = {},
//        onTogglePlayPause = {},
//        onForward3s = {},
//        onBackward3s = {}
//    )

    ButtonsUi(
        isPlaying = true,
        onTogglePlayPause = {},
        onForward3s = {},
        onBackward3s = {}
    )
}
