package com.example.pexel.ui.components

import android.net.Uri
import android.view.View
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView

@OptIn(UnstableApi::class)
@Composable
fun VideoPlayerView(videoUrl: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val exoPlayer = remember(videoUrl) {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri(Uri.parse(videoUrl))
            setMediaItem(mediaItem)

            addListener(object : Player.Listener {
                override fun onPlayerError(error: PlaybackException) {
                    Log.e("VideoPlayer", "Playback error: ${error.message}", error)
                }

                override fun onPlaybackStateChanged(playbackState: Int) {
                    when (playbackState) {
                        Player.STATE_BUFFERING -> Log.d("VideoPlayer", "Buffering…")
                        Player.STATE_READY -> Log.d("VideoPlayer", "Ready to play")
                        Player.STATE_ENDED -> Log.d("VideoPlayer", "Playback ended")
                        Player.STATE_IDLE -> Log.d("VideoPlayer", "Idle")
                    }
                }
            })

            playWhenReady = true
            prepare()
        }
    }

    DisposableEffect(exoPlayer) {
        onDispose { exoPlayer.release() }
    }

    AndroidView(
        factory = { ctx ->
            PlayerView(ctx).apply {
                player = exoPlayer
                useController = true
                controllerShowTimeoutMs = 3000
                controllerHideOnTouch = true
                // Enable fullscreen mode
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                // Hide system UI for true fullscreen experience
                systemUiVisibility = (
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        )
            }
        },
        modifier = modifier,
        update = { playerView ->
            // Update fullscreen settings when recomposed
            playerView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    )
        }
    )
}