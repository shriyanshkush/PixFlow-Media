import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.pexel.data.remote.models.photo.PexelsPhoto
import com.example.pexel.ui.components.HeartButton

@Composable
fun ImageCard(
    photo: PexelsPhoto,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Box {
            // Load image using Coil
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(photo.src.medium)
                    .crossfade(true)
                    .build(),
                contentDescription = photo.alt ?: "Photo",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Gradient overlay for better text readability
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

            // Heart button in top-right corner
            HeartButton(
                isFavorite = isFavorite,
                onToggle = onToggleFavorite,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            )

            // Photo info overlay
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
            ) {
                Text(
                    text = photo.photographer,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Dimensions",
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${photo.width}x${photo.height}",
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}