package com.example.pexel.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import com.example.pexel.ui.screens.ImageDetail.ImageDetailScreen
import com.example.pexel.ui.screens.VideoDetails.VideoDetailScreen
import com.example.pexel.ui.screens.favorite.FavoriteScreen
import com.example.pexel.ui.screens.home.HomeScreen

@Composable
fun NavigationSystem() {
    val navController = rememberNavController()

    NavHost(
        startDestination = Routes.Home,
        navController = navController,
    ) {
        composable<Routes.Home> {
            HomeScreen(navController)
        }


        composable<Routes.Favourite> {
            FavoriteScreen(navController)
        }

        composable<Routes.ImageDetail> { backStackEntry ->
            val imageDetail = backStackEntry.toRoute<Routes.ImageDetail>()
            ImageDetailScreen(
                photoId = imageDetail.photoId,
                navController = navController
            )
        }

        composable<Routes.VideoDetail> { backStackEntry ->
            val videoDetail = backStackEntry.toRoute<Routes.VideoDetail>()
            VideoDetailScreen(
                videoId = videoDetail.videoId,
                navController = navController
            )
        }
    }
}