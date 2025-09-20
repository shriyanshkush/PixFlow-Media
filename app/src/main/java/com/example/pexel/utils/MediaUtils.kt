package com.example.pexel.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.core.content.FileProvider
import coil.ImageLoader
import coil.request.ImageRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL

object MediaUtils {

    suspend fun downloadImage(
        context: Context,
        imageUrl: String,
        fileName: String
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            val imageLoader = ImageLoader(context)
            val request = ImageRequest.Builder(context)
                .data(imageUrl)
                .build()

            val drawable = imageLoader.execute(request).drawable
            val bitmap = (drawable as? BitmapDrawable)?.bitmap
                ?: return@withContext false

            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val imageFile = File(downloadsDir, "$fileName.jpg")

            FileOutputStream(imageFile).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            }

            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Image saved to Downloads", Toast.LENGTH_SHORT).show()
            }
            true
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Failed to download image: ${e.message}", Toast.LENGTH_SHORT).show()
            }
            false
        }
    }

    suspend fun downloadVideo(
        context: Context,
        videoUrl: String,
        fileName: String
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            val url = URL(videoUrl)
            val connection = url.openConnection()
            connection.connect()

            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val videoFile = File(downloadsDir, "$fileName.mp4")

            connection.getInputStream().use { input ->
                FileOutputStream(videoFile).use { output ->
                    input.copyTo(output)
                }
            }

            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Video saved to Downloads", Toast.LENGTH_SHORT).show()
            }
            true
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Failed to download video: ${e.message}", Toast.LENGTH_SHORT).show()
            }
            false
        }
    }

    fun shareImage(context: Context, imageUrl: String, fileName: String = "shared_image") {
        try {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, "Check out this amazing image: $imageUrl")
                putExtra(Intent.EXTRA_SUBJECT, "Amazing Image from Pexel")
            }

            val chooserIntent = Intent.createChooser(shareIntent, "Share Image")
            chooserIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(chooserIntent)
        } catch (e: Exception) {
            Toast.makeText(context, "Failed to share image", Toast.LENGTH_SHORT).show()
        }
    }

    fun shareVideo(context: Context, videoUrl: String, fileName: String = "shared_video") {
        try {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, "Check out this amazing video: $videoUrl")
                putExtra(Intent.EXTRA_SUBJECT, "Amazing Video from Pexel")
            }

            val chooserIntent = Intent.createChooser(shareIntent, "Share Video")
            chooserIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(chooserIntent)
        } catch (e: Exception) {
            Toast.makeText(context, "Failed to share video", Toast.LENGTH_SHORT).show()
        }
    }
}
