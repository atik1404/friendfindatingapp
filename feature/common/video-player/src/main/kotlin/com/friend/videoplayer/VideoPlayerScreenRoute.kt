package com.friend.videoplayer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun VideoPlayerScreenRoute(
    videoUrl: String,
    viewModel: VideoPlayerViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    // Load/replace URL
    LaunchedEffect(videoUrl) {
        viewModel.setUrl(videoUrl)
    }
    VideoPlayerScreen(
        state = uiState,
        exoPlayer = viewModel.player,
        onStart = viewModel::onStart,
        onStop = viewModel::onStop,
        onForward = viewModel::seekBy,
        onClose = onBackClick
    )
}