package com.kl3jvi.yonda.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.tooling.preview.Preview
import com.kl3jvi.yonda.ext.sortDevices
import com.kl3jvi.yonda.models.DiscoveredBluetoothDevice
import com.kl3jvi.yonda.ui.components.ScannedBleDeviceCard

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    scooterData: ScanState,
    onClick: (DiscoveredBluetoothDevice) -> Unit = {},
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
                contentAlignment = Alignment.Center,
            ) {
                Column(modifier.align(Alignment.Center)) {
                    CircularProgressIndicator()
                    Text("Searching for devices...")
                }
            }
        }

        is ScanState.Stopped -> {
            DisplayRememberedDevicesOrRetryMessage(
                rememberedDevices,
                modifier,
                onClick,
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DisplayRememberedDevicesOrRetryMessage(
    rememberedDevices: List<DiscoveredBluetoothDevice>,
    modifier: Modifier = Modifier,
    onClick: (DiscoveredBluetoothDevice) -> Unit = {},
) {
    if (rememberedDevices.isNotEmpty()) {
        LazyColumn {
            items(
                rememberedDevices.sortDevices(),
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
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        scooterData =
            ScanState.Founded(
                emptyList(),
            ),
    )
}
