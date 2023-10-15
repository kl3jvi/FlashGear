package com.kl3jvi.yonda.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kl3jvi.yonda.ext.sortDevices
import com.kl3jvi.yonda.models.DiscoveredBluetoothDevice
import com.kl3jvi.yonda.ui.components.FlashGearButtonGradient
import com.kl3jvi.yonda.ui.components.ScannedBleDeviceCard

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    scooterData: ScanState,
    onClick: (DiscoveredBluetoothDevice) -> Unit = {},
    restartScan: () -> Unit = {},
) {
    var rememberedDevices by remember { mutableStateOf(listOf<DiscoveredBluetoothDevice>()) }

    when (scooterData) {
        is ScanState.Founded -> {
            rememberedDevices = scooterData.devices
            LazyColumn {
                items(
                    scooterData.devices.sortDevices(),
                    key = { it.address },
                ) { scooter ->
                    Box(modifier = Modifier.animateItemPlacement()) {
                        ScannedBleDeviceCard(
                            modifier = modifier,
                            discoveredBluetoothDevice = scooter,
                        ) { onClick(scooter) }
                    }
                }
            }
        }

        ScanState.Searching -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
                Text("Searching for devices...")
            }
        }

        is ScanState.Stopped -> {
            DisplayRememberedDevicesOrRetryMessage(
                rememberedDevices,
                modifier,
                onClick,
                restartScan
            )
        }

        is ScanState.Timeout -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Scanning timed out. Tap to try again.",
                    modifier = Modifier.clickable { restartScan() })
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DisplayRememberedDevicesOrRetryMessage(
    rememberedDevices: List<DiscoveredBluetoothDevice>,
    modifier: Modifier = Modifier,
    onClick: (DiscoveredBluetoothDevice) -> Unit = {},
    restartScan: () -> Unit = {},
) {
    if (rememberedDevices.isNotEmpty()) {
        LazyColumn {
            items(
                rememberedDevices.sortDevices(),
                key = { it.address }
            ) { scooter ->
                Box(modifier = Modifier.animateItemPlacement()) {
                    ScannedBleDeviceCard(
                        modifier = modifier,
                        discoveredBluetoothDevice = scooter,
                    ) { onClick(scooter) }
                }
            }
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            FlashGearButtonGradient(
                onClick = { restartScan() },
                text = "Restart Scan"
            )
        }
    }
}

@Composable
fun isOnHomeScreen(navController: NavHostController): Boolean {
    // Get the current route from the NavController
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    return currentRoute == Screen.Home.route
}

