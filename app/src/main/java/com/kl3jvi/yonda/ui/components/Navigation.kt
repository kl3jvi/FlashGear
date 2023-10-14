package com.kl3jvi.yonda.ui.components

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kl3jvi.yonda.ui.screens.HomeScreen
import com.kl3jvi.yonda.ui.screens.ScanState
import com.kl3jvi.yonda.ui.screens.Screen

@Composable
fun NavigationGraph(
    scooterData: ScanState,
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.Permission.route,
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController, scooterData = scooterData)
        }
        composable(Screen.Permission.route) {
            PermissionsScreen(navController = navController)
        }
    }
}
