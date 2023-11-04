package com.kl3jvi.yonda.ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kl3jvi.yonda.ble.model.DiscoveredBluetoothDevice
import com.kl3jvi.yonda.ble.scanner.ScanningState
import com.kl3jvi.yonda.ui.components.ScanErrorView
import com.kl3jvi.yonda.ui.components.ScannedBleDeviceCard

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    state: ScanningState,
    onClick: (DiscoveredBluetoothDevice) -> Unit = {},
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 16.dp),
    ) {
        when (state) {
            is ScanningState.Loading -> item { ScanEmptyView(false) }
            is ScanningState.DevicesDiscovered -> {
                if (state.isEmpty()) {
                    item { ScanEmptyView() }
                } else {
                    items(state.devices) {
                        ScannedBleDeviceCard(
                            modifier = modifier,
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
