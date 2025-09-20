package com.example.pexel.ui.screens.VideoDetails

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.pexel.R
import com.example.pexel.data.remote.models.video.PexelVideo
import com.example.pexel.presentation.viewmodel.VideoDetailUiState
import com.example.pexel.presentation.viewmodel.VideoDetailViewModel
import com.example.pexel.ui.screens.ImageDetail.TopBar
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.example.pexel.presentation.viewmodel.FavoriteViewModel
import com.example.pexel.ui.components.VideoPlayerView

@OptIn(UnstableApi::class)
@Composable
fun VideoDetailScreen(
    videoId: String,
    navController: NavHostController,
    viewModel: VideoDetailViewModel = hiltViewModel(),
    favoriteViewModel: FavoriteViewModel= hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showPlayer by remember { mutableStateOf(false) }

    val context = LocalContext.current

    LaunchedEffect(videoId) {
        viewModel.loadVideoDetails(videoId)
    }

    val isFavorite by favoriteViewModel.isFavorite(videoId).collectAsState(initial = false)

    Scaffold(
        modifier = Modifier.systemBarsPadding(),
        topBar = {
            TopBar(
                onBackClick = { navController.popBackStack() },
                isFavorite = isFavorite,
                onToggleFavorite = {
                    when (val state = uiState) {
                        is VideoDetailUiState.Loading -> {
                            Toast.makeText(context, "Loading...", Toast.LENGTH_SHORT).show()
                        }

                        is VideoDetailUiState.Success -> {
                            viewModel.toggleVideoFavorite(state.video)
                        }

                        else -> {
                            Toast.makeText(context,"Loading Failed ...", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        when (val state = uiState) {
            is VideoDetailUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is VideoDetailUiState.Success -> {
                // Fixed video URL selection logic
                val videoUrl = getBestVideoUrl(state.video)

                VideoDetailContent(
                    video = state.video,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 12.dp),
                    onPlayClick = {
                        if (videoUrl != null) {
                            showPlayer = true
                        } else {
                            Log.e("VideoDetail", "No video URL available!")
                        }
                    }
                )

                if (showPlayer && videoUrl != null) {
                    FullscreenVideoPlayer(
                        videoUrl = videoUrl,
                        onDismiss = { showPlayer = false }
                    )
                }
            }

            is VideoDetailUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Error loading video",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.loadVideoDetails(videoId) }) {
                            Text("Retry")
                        }
                    }
                }
            }

            else -> {}
        }
    }
}

// Helper function to get the best available video URL
@OptIn(UnstableApi::class)
private fun getBestVideoUrl(video: PexelVideo): String? {
    val videoFiles = video.videoFiles

    // Enhanced debugging with null safety
    Log.d("VideoDetail", "Video ID: ${video.id}")
    Log.d("VideoDetail", "Video files is null: ${videoFiles == null}")

    if (videoFiles == null) {
        Log.e("VideoDetail", "Video files list is null - deserialization issue!")
        return null
    }

    Log.d("VideoDetail", "Video files count: ${videoFiles.size}")
    Log.d("VideoDetail", "Video files empty: ${videoFiles.isEmpty()}")

    if (videoFiles.isEmpty()) {
        Log.e("VideoDetail", "Video files list is empty")
        return null
    }

    // Log available video files for debugging
    Log.d("VideoDetail", "Available video files:")
    videoFiles.forEachIndexed { index, file ->
        Log.d("VideoDetail", "[$index] Quality: ${file.quality}, Width: ${file.width}, Link: ${file.link}")
    }

    // Try to get the best quality video in order of preference
    return videoFiles.find { it.quality == "hd" && it.width == 1080 }?.link
        ?: videoFiles.find { it.quality == "hd" }?.link
        ?: videoFiles.find { it.quality == "sd" && it.width == 720 }?.link
        ?: videoFiles.find { it.quality == "sd" }?.link
        ?: videoFiles.firstOrNull()?.link
}

@Composable
private fun VideoDetailContent(
    video: PexelVideo,
    modifier: Modifier = Modifier,
    onPlayClick: () -> Unit
) {
    val context = LocalContext.current
    val videoUrl = getBestVideoUrl(video)

    Column(modifier = modifier) {
        HorizontalDivider(modifier = Modifier.height(1.dp))

        Spacer(modifier = Modifier.height(10.dp))

        // Video thumbnail with play button
        Box(
            modifier = Modifier
                .height(300.dp)
                .fillMaxWidth()
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(video.image)
                    .crossfade(true)
                    .build(),
                contentDescription = "Video thumbnail",
                modifier = Modifier.matchParentSize()
            )

            IconButton(
                onClick = onPlayClick,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(64.dp)
                    .background(
                        color = Color.Black.copy(alpha = 0.5f),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play Video",
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Video by ${video.user.name}",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Resolution: ${video.width}x${video.height}\nDuration: ${formatDuration(video.duration)}",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Action Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    videoUrl?.let { downloadVideo(context, it, "pexels_video_${video.id}.mp4") }
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                enabled = videoUrl != null
            ) {
                Row {
                    Icon(
                        painter = painterResource(R.drawable.download_minimalistic_svgrepo_com),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Download")
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Button(
                onClick = {
                    videoUrl?.let { shareVideo(context, it) }
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                border = BorderStroke(width = 1.dp, color = Color.Gray),
                enabled = videoUrl != null
            ) {
                Row {
                    Icon(
                        painter = painterResource(R.drawable.share_svgrepo_com),
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = Color.Black
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Share", color = Color.Black)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun FullscreenVideoPlayer(videoUrl: String, onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false // This makes it full width
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            VideoPlayerView(
                videoUrl = videoUrl,
                modifier = Modifier.fillMaxSize()
            )

            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .statusBarsPadding()
                    .size(36.dp)
                    .background(Color.Black.copy(alpha = 0.7f), CircleShape)
            ) {
                Icon(
                    painter = painterResource(R.drawable.close_circle_svgrepo_com),
                    contentDescription = "Close",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}


private fun formatDuration(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return "$minutes:${remainingSeconds.toString().padStart(2, '0')}"
}

private fun downloadVideo(context: Context, url: String, fileName: String) {
    val request = DownloadManager.Request(Uri.parse(url))
        .setTitle("Downloading Video")
        .setDescription("Saving video...")
        .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

    val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    downloadManager.enqueue(request)
}

private fun shareVideo(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, url)
    }
    context.startActivity(Intent.createChooser(intent, "Share Video"))
}