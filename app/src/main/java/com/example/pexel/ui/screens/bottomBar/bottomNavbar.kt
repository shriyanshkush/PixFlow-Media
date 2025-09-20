package com.example.pexel.ui.screens.bottomBar

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.pexel.R
import com.example.pexel.ui.navigation.Routes

@Composable
fun BottomNavbar(
    navController: NavController
) {
    val items = listOf(
        NavigationItem("Home", R.drawable.home_4_svgrepo_com, Routes.Home::class.qualifiedName!!),
        NavigationItem("Favourite", R.drawable.heart_svgrepo_com, Routes.Favourite::class.qualifiedName!!),
        //NavigationItem("Downloads", R.drawable.download_minimalistic_svgrepo_com, "downloads")
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        items.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                modifier = Modifier.padding(top = 4.dp),
                selected = currentRoute == item.route,
                onClick = {
                    if (!selected) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = item.iconRes),
                        contentDescription = item.name,
                        modifier = Modifier.size(28.dp) // smaller than 32 for balance
                    )
                },
                label = {
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        }
    }
}



data class NavigationItem(
    val name: String,
    @DrawableRes val iconRes: Int,
    val route: String
)
