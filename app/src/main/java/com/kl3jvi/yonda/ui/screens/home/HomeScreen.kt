package com.kl3jvi.yonda.ui.screens.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.wear.compose.material.Text
import com.kl3jvi.yonda.ble.model.DiscoveredBluetoothDevice
import com.kl3jvi.yonda.ble.scanner.ScanningState
import com.kl3jvi.yonda.ui.components.ScanEmptyView
import com.kl3jvi.yonda.ui.components.ScanErrorView
import com.kl3jvi.yonda.ui.components.common.ScannedBleDeviceCard

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    state: ScanningState,
    onClick: (DiscoveredBluetoothDevice) -> Unit = {},
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 16.dp),
    ) {
        when (state) {
            is ScanningState.Loading -> item { ScanEmptyView(modifier, false) }
            is ScanningState.DevicesDiscovered -> {
                if (state.isEmpty()) {
                    item { ScanEmptyView() }
                } else {
                    item {
                        Text(
                            color = MaterialTheme.colorScheme.primary,
                            text = "${state.devices.size} Devices Found",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = modifier.padding(start = 16.dp, bottom = 4.dp),
                        )
                    }
                    items(state.devices, key = { it.address }) {
                        ScannedBleDeviceCard(
                            modifier = modifier.animateItemPlacement(),
                            discoveredBluetoothDevice = it,
                            onClick = onClick,
                        )
                    }
                }
            }

            is ScanningState.Error -> item { ScanErrorView(state.errorCode) }
        }
    }
}
