package com.kl3jvi.yonda.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kl3jvi.yonda.models.DiscoveredBluetoothDevice
import com.kl3jvi.yonda.ui.screens.HomeScreen
import com.kl3jvi.yonda.ui.screens.ScanState
import com.kl3jvi.yonda.ui.screens.Screen

@Composable
fun NavigationGraph(
    scooterData: ScanState,
    isOnHome: (Boolean) -> Unit,
    connectDevice: (DiscoveredBluetoothDevice) -> Unit,
    restartScan: () -> Unit = {},
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.Permission.route,
    ) {
        composable(Screen.Home.route) {
            isOnHome(true)
            Column {
                HomeScreen(
                    scooterData = scooterData,
                    onClick = connectDevice,
                    restartScan = restartScan
                )
            }
        }
        composable(Screen.Permission.route) {
            isOnHome(false)
            PermissionsScreen(navController = navController)
        }
    }
}
