package com.example.pexel.ui.screens.ImageDetail

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.pexel.R
import com.example.pexel.data.remote.models.photo.PexelsPhoto
import com.example.pexel.presentation.viewmodel.FavoriteViewModel
import com.example.pexel.presentation.viewmodel.ImageDetailUiState
import com.example.pexel.presentation.viewmodel.ImageDetailViewModel
import com.example.pexel.utils.MediaUtils
import kotlinx.coroutines.launch

@Composable
fun ImageDetailScreen(
    photoId: String,
    navController: NavHostController,
    viewModel: ImageDetailViewModel = hiltViewModel(),
    favoriteViewModel: FavoriteViewModel= hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(photoId) {
        viewModel.loadPhotoDetails(photoId)
    }

    val isFavorite by favoriteViewModel.isFavorite(photoId).collectAsState(initial = false)

    Scaffold(
        modifier = Modifier.systemBarsPadding(),
        topBar = {
            TopBar(
                onBackClick = { navController.popBackStack() },
                isFavorite = isFavorite,
                onToggleFavorite = {
                    when (val state = uiState) {
                        is ImageDetailUiState.Loading -> {
                            Toast.makeText(context, "Loading...", Toast.LENGTH_SHORT).show()
                        }

                        is ImageDetailUiState.Success -> {
                            viewModel.togglePhotoFavorite(state.photo)
                        }

                        else -> {
                            Toast.makeText(context,"Loading Failed ...",Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            )
        }
    ) { paddingValues ->
        when (val state = uiState) {
            is ImageDetailUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is ImageDetailUiState.Success -> {
                ImageDetailContent(
                    photo = state.photo,
                    onDownload = { photo ->
                        scope.launch {
                            MediaUtils.downloadImage(
                                context = context,
                                imageUrl = photo.src.original,
                                fileName = "pexel_${photo.id}_${photo.photographer.replace(" ", "_")}"
                            )
                        }
                    },
                    onShare = { photo ->
                        MediaUtils.shareImage(
                            context = context,
                            imageUrl = photo.src.original,
                            fileName = "pexel_${photo.id}"
                        )
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 12.dp)
                        .verticalScroll(rememberScrollState())
                )
            }

            is ImageDetailUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Error loading image",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.loadPhotoDetails(photoId) }) {
                            Text("Retry")
                        }
                    }
                }
            }

            else -> {}
        }
    }
}

@Composable
private fun ImageDetailContent(
    photo: PexelsPhoto,
    onDownload: (PexelsPhoto) -> Unit,
    onShare: (PexelsPhoto) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        HorizontalDivider(modifier = Modifier.height(1.dp))

        Spacer(modifier = Modifier.height(10.dp))

        // Main Image
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(photo.src.large)
                .crossfade(true)
                .build(),
            contentDescription = photo.alt,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(400.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Photographer card
        Card(
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(width = 1.dp, color = Color.Gray),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.person_svgrepo_com),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(Color.Transparent),
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = photo.photographer,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Photographer",
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                    }
                }

                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.heart_svgrepo_com),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = Color.Red
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${photo.width}x${photo.height}",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.width(20.dp))

                    Text(text = "Avg. Color:", fontSize = 16.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.width(4.dp))
                    val avgColorHex = photo.avgColor
                    val avgColorInt = try {
                        if (!avgColorHex.isNullOrBlank()) {
                            android.graphics.Color.parseColor(avgColorHex)
                        } else {
                            android.graphics.Color.GRAY // fallback
                        }
                    } catch (e: IllegalArgumentException) {
                        android.graphics.Color.GRAY // fallback if invalid hex
                    }

                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(RectangleShape)
                            .background(Color(avgColorInt))
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = avgColorHex ?: "N/A",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )

                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Action Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { onDownload(photo) },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
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
                onClick = { onShare(photo) },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                border = BorderStroke(width = 1.dp, color = Color.Gray),
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