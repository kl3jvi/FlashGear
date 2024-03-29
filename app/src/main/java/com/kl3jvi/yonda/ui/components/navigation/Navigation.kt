package com.kl3jvi.yonda.ui.components.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kl3jvi.yonda.ble.model.DiscoveredBluetoothDevice
import com.kl3jvi.yonda.ble.scanner.ScanningState
import com.kl3jvi.yonda.ui.screens.details.DeviceScreen
import com.kl3jvi.yonda.ui.screens.home.HomeScreen
import com.kl3jvi.yonda.ui.screens.permissions.PermissionsScreen

@Composable
fun NavigationGraph(
    scanningState: ScanningState,
    connectDevice: (DiscoveredBluetoothDevice) -> Unit,
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.Permission.route,
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                navController = navController,
                state = scanningState,
                onClick = connectDevice,
            )
        }
        composable(Screen.Permission.route) {
            PermissionsScreen(navController = navController)
        }

        composable(Screen.Device.route) {
            DeviceScreen()
        }
    }
}
