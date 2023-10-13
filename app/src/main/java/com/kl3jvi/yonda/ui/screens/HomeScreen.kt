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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.kl3jvi.yonda.ext.sortDevices
import com.kl3jvi.yonda.models.DiscoveredBluetoothDevice
import com.kl3jvi.yonda.ui.components.ScannedBleDeviceCard

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    scooterData: ScanState,
    onClick: (DiscoveredBluetoothDevice) -> Unit = {},
    restartScan: () -> Unit = {},
) {
    when (scooterData) {
        is ScanState.Founded -> {
            LazyColumn {
                items(
                    scooterData.devices.sortDevices(),
                    key = { it.address },
                ) { scooter ->
                    Box(modifier = Modifier.animateItemPlacement()) {
                        ScannedBleDeviceCard(
                            modifier = modifier,
                            discoveredBluetoothDevice = scooter,
                        ) {
                            onClick(scooter)
                        }
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
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Scanning stopped. Tap to search again.",
                    modifier = Modifier.clickable { restartScan() })
            }
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
