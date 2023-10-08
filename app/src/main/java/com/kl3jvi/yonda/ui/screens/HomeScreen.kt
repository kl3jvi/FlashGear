package com.kl3jvi.yonda.ui.screens

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.kl3jvi.yonda.ui.components.BleStatusView

@Composable
fun HomeScreen() {
    val context = LocalContext.current
    val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    val bluetoothAdapter = bluetoothManager.adapter

    if (bluetoothAdapter == null) {
        // Device doesn't support Bluetooth
    } else if (!bluetoothAdapter.isEnabled) {
        // Bluetooth is disabled
        BleStatusView {
            bluetoothAdapter.enable()
        }
    } else {
        // Bluetooth is enabled
        // Show other parts of your home screen here
    }
}
