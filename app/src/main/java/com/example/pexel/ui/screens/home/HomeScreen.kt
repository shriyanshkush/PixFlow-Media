package com.example.pexel.ui.screens.home

import ImageCard
import VideoCard
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.pexel.presentation.viewmodel.FavoriteViewModel
import com.example.pexel.presentation.viewmodel.PexelHomeViewModel
import com.example.pexel.ui.navigation.Routes
import com.example.pexel.ui.screens.bottomBar.BottomNavbar

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: PexelHomeViewModel = hiltViewModel(),
    favoriteViewModel: FavoriteViewModel = hiltViewModel()
) {
    val selectedTab by viewModel.selectedTab.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    val photosPagingData = viewModel.photos.collectAsLazyPagingItems()
    val videosPagingData = viewModel.videos.collectAsLazyPagingItems()

    val tabs = listOf("Photos", "Videos")
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        bottomBar = {
            BottomNavbar(navController = navController)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
        ) {
            // App Title
            Text(
                text = "Pexel Explorer",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // Search Bar
            SearchBar(
                query = searchQuery,
                onQueryChange = viewModel::updateSearchQuery,
                onSearch = {
                    keyboardController?.hide()
                },
                onClear = viewModel::clearSearch,
                placeholder = if (selectedTab == 0) "Search photos..." else "Search videos...",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // Tab Selector
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .background(Color(0xFFD7D7DA), RoundedCornerShape(50))
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                tabs.forEachIndexed { index, title ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(50))
                            .background(
                                if (selectedTab == index) Color.White else Color.Transparent
                            )
                            .clickable { viewModel.selectTab(index) }
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

            Spacer(modifier = Modifier.height(6.dp))

            // Section Title
            Text(
                text = if (searchQuery.isNotBlank()) {
                    if (selectedTab == 0) "Search Results - Photos" else "Search Results - Videos"
                } else {
                    if (selectedTab == 0) "Popular Photos" else "Popular Videos"
                },
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Content Grid with Paging
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                if (selectedTab == 0) {
                    // Photos tab
                    items(photosPagingData.itemCount) { index ->
                        photosPagingData[index]?.let { photo ->
                            val isFavorite by favoriteViewModel.isFavorite(photo.id.toString()).collectAsState(initial = false)
                            ImageCard(
                                photo = photo,
                                isFavorite = isFavorite,
                                onToggleFavorite = {
                                    viewModel.togglePhotoFavorite(photo)
                                },
                                onClick = {
                                    // Navigate to photo detail screen
                                    navController.navigate(Routes.ImageDetail(photo.id.toString()))
                                }
                            )
                        }
                    }

                    // Handle loading and error states for photos
                    photosPagingData.apply {
                        when {
                            loadState.refresh is LoadState.Loading -> {
                                item(span = { GridItemSpan(2) }) {
                                    LoadingItem()
                                }
                            }

                            loadState.refresh is LoadState.Error -> {
                                item(span = { GridItemSpan(2) }) {
                                    ErrorItem(
                                        message = "Failed to load photos",
                                        onRetry = { retry() }
                                    )
                                }
                            }

                            loadState.append is LoadState.Loading -> {
                                item(span = { GridItemSpan(2) }) {
                                    LoadingItem()
                                }
                            }

                            loadState.append is LoadState.Error -> {
                                item(span = { GridItemSpan(2) }) {
                                    ErrorItem(
                                        message = "Failed to load more photos",
                                        onRetry = { retry() }
                                    )
                                }
                            }
                        }
                    }
                } else {
                    // Videos tab
                    items(videosPagingData.itemCount) { index ->
                        videosPagingData[index]?.let { video ->
                            val isFavorite by favoriteViewModel.isFavorite(video.id.toString()).collectAsState(initial = false)
                            VideoCard(
                                video = video,
                                isFavorite = isFavorite,
                                onToggleFavorite = {
                                    viewModel.toggleVideoFavorite(video)
                                },
                                onClick = {
                                    // Navigate to video detail screen
                                    navController.navigate(Routes.VideoDetail(video.id.toString()))
                                }
                            )
                        }
                    }

                    // Handle loading and error states for videos
                    videosPagingData.apply {
                        when {
                            loadState.refresh is LoadState.Loading -> {
                                item(span = { GridItemSpan(2) }) {
                                    LoadingItem()
                                }
                            }

                            loadState.refresh is LoadState.Error -> {
                                item(span = { GridItemSpan(2) }) {
                                    ErrorItem(
                                        message = "Failed to load videos",
                                        onRetry = { retry() }
                                    )
                                }
                            }

                            loadState.append is LoadState.Loading -> {
                                item(span = { GridItemSpan(2) }) {
                                    LoadingItem()
                                }
                            }

                            loadState.append is LoadState.Error -> {
                                item(span = { GridItemSpan(2) }) {
                                    ErrorItem(
                                        message = "Failed to load more videos",
                                        onRetry = { retry() }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onClear: () -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = {
            Text(
                text = placeholder,
                color = Color.Gray,
                fontSize = 14.sp
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color.Gray
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = onClear) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear search",
                        tint = Color.Gray
                    )
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = Color.Gray.copy(alpha = 0.3f),
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearch()
            }
        )
    )
}

@Composable
private fun LoadingItem() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(40.dp),
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun ErrorItem(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text("Retry")
        }
    }
}