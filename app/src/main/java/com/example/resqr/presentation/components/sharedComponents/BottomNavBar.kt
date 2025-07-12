package com.example.resqr.presentation.components.sharedComponents

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState


sealed class BottomNav(val screens: String, val icons: ImageVector, val descriptions: String) {
    sealed class BottomNavItems(route: String, icon: ImageVector, descriptions: String) : BottomNav(
        screens = route,
        icons = icon,
        descriptions = descriptions
    ) {

        object Emergency : BottomNavItems(
            route = "victim_home_screen",
            icon = Icons.Default.Favorite,
            descriptions = "Emergency"
        )

        object Responder : BottomNavItems(
            route = "responder_home_screen",
            icon = Icons.Default.HealthAndSafety,
            descriptions = "Responder"
        )

    }
}

@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(
        BottomNav.BottomNavItems.Emergency,
        BottomNav.BottomNavItems.Responder
    )

    val currentDestination = navController
        .currentBackStackEntryAsState().value?.destination

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        tonalElevation = 4.dp
    ) {
        items.forEach { item ->
            val selected = currentDestination?.route == item.screens

            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (!selected) {
                        navController.navigate(item.screens) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icons,
                        contentDescription = item.descriptions
                    )
                },
                label = {
                    Text(
                        text = item.descriptions,
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                alwaysShowLabel = true,
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                    selectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    selectedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            )
        }
    }
}