package com.friendfinapp.dating.ui.videoplay

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.friendfinapp.dating.R
import com.friendfinapp.dating.application.BaseActivity
import com.friendfinapp.dating.databinding.ActivityVideoViewPlayBinding
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util


class VideoViewPlayActivity : BaseActivity<ActivityVideoViewPlayBinding>() {


    private var player: ExoPlayer? = null

    var videoUrl = ""
    override fun viewBindingLayout(): ActivityVideoViewPlayBinding =
        ActivityVideoViewPlayBinding.inflate(layoutInflater)

    override fun initializeView(savedInstanceState: Bundle?) {
        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            videoUrl = bundle.getString("videoUrl").toString() // 1

            Log.d("TAG", "onCreate: $videoUrl")
        }

        setUpClickListener()

        // Create a default TrackSelector
        // Create a default TrackSelector
        val trackSelector = DefaultTrackSelector()

        // Create a default LoadControl

        // Create a default LoadControl
        val loadControl = DefaultLoadControl()

        // Create a SimpleExoPlayer with default components

        // Create a SimpleExoPlayer with default components
        player = SimpleExoPlayer.Builder(this, DefaultRenderersFactory(this))
            .setLoadControl(loadControl)
            .setTrackSelector(trackSelector)
            .build()

        binding.videoView.player = player


        // Prepare the MediaSource
        val videoUri = Uri.parse(videoUrl)


        val mediaItem = MediaItem.fromUri(videoUri)

        // Set the media item to the player
        player?.setMediaItem(mediaItem)

        // Set up a progressive media source with preloading
        val dataSourceFactory = DefaultDataSourceFactory(
            this,
            Util.getUserAgent(this, "FriendFin")
        )
        val progressiveMediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(mediaItem)


        // Listener to hide the progressBar when the player is prepared
        player?.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                if (isPlaying) {
                    binding.progressBar.visibility = View.GONE
                }
            }
        })

        // Prepare the player
        player?.prepare(progressiveMediaSource)

// Start preloading the video
        player?.playWhenReady = false


    }

    private fun setUpClickListener() {

        binding.imageBack.setOnClickListener {
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24) {
            player?.playWhenReady = true
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) {
            player?.playWhenReady = false
        }
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT < 24) {
            player?.playWhenReady = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }

    private fun releasePlayer() {
        player?.release()
        player = null
    }
}