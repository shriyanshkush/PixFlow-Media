package com.example.pexel.ui.screens.favorite

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.example.pexel.data.local.entities.FavoriteEntity
import com.example.pexel.presentation.viewmodel.FavoriteViewModel
import com.example.pexel.ui.navigation.Routes
import com.example.pexel.ui.screens.bottomBar.BottomNavbar

@Composable
fun FavoriteScreen(
    navController: NavHostController,
    viewModel: FavoriteViewModel = hiltViewModel()
) {
    val favorites by viewModel.favorites.collectAsState()
    val selectedFilter by viewModel.selectedFilter.collectAsState()
    val favoritesCount by viewModel.favoritesCount.collectAsState()

    val filterOptions = listOf(
        "all" to "All ($favoritesCount)",
        "photos" to "Photos",
        "videos" to "Videos"
    )

    Scaffold(
        bottomBar = {
            BottomNavbar(navController = navController)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            // Header
            Text(
                text = "My Favorites",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // Filter tabs
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .background(Color(0xFFF2F2F6), RoundedCornerShape(50))
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                filterOptions.forEach { (filter, title) ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(50))
                            .background(
                                if (selectedFilter == filter) Color.White else Color.Transparent
                            )
                            .clickable { viewModel.setFilter(filter) }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = title,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black
                        )
                    }
                }
            }

            // Favorites grid
            if (favorites.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = "No favorites",
                            modifier = Modifier.size(64.dp),
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No favorites yet",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.Gray
                        )
                        Text(
                            text = "Tap the heart icon on photos and videos to save them here",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(favorites) { favorite ->
                        FavoriteItem(
                            favorite = favorite,
                            onRemove = { viewModel.removeFavorite(favorite.id) },
                            onClick = {
                                if (favorite.type == "photo") {
                                    navController.navigate(Routes.ImageDetail(favorite.id))
                                } else {
                                    navController.navigate(Routes.VideoDetail(favorite.id))
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FavoriteItem(
    favorite: FavoriteEntity,
    onRemove: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box {
            // Image/Video thumbnail
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(favorite.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = favorite.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Gradient overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        androidx.compose.ui.graphics.Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f)
                            ),
                            startY = 100f
                        )
                    )
            )

            // Remove button
            IconButton(
                onClick = onRemove,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remove from favorites",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Type indicator
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp)
                    .background(
                        if (favorite.type == "video") Color.Red else Color.Blue,
                        RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = if (favorite.type == "video") painterResource(R.drawable.download_minimalistic_svgrepo_com) else painterResource(R.drawable.image_svgrepo_com),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = favorite.type.uppercase(),
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Download indicator
            if (favorite.isDownloaded) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                        .background(Color.Green, CircleShape)
                        .padding(4.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.download_minimalistic_svgrepo_com),
                        contentDescription = "Downloaded",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // Info overlay
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp)
            ) {
                Text(
                    text = favorite.creator,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Text(
                    text = "${favorite.width}x${favorite.height}",
                    color = Color.White,
                    fontSize = 10.sp
                )
            }
        }
    }
}