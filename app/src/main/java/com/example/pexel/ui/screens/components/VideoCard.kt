// components/VideoCard.kt
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.pexel.data.remote.models.video.PexelVideo
import com.example.pexel.ui.components.HeartButton

@Composable
fun VideoCard(
    video: PexelVideo,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.background(Color.White)
        ) {
            // Thumbnail with play button + duration + heart
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(video.image)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Video Thumbnail",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Play button in center
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.Center)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.9f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Heart button in top-right corner
                HeartButton(
                    isFavorite = isFavorite,
                    onToggle = onToggleFavorite,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                )

                // Duration at top-right (moved left because of heart)
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color.Black.copy(alpha = 0.7f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = formatDuration(video.duration),
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Video Info
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = video.user?.name ?: "Unknown Creator",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "${video.width}x${video.height}",
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }
        }
    }
}
// Helper function to format video duration
private fun formatDuration(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return "$minutes:${remainingSeconds.toString().padStart(2, '0')}"
}