package com.kl3jvi.yonda.ble.scanner

import com.kl3jvi.yonda.ble.model.DiscoveredBluetoothDevice

sealed class ScanningState {

    data object Loading : ScanningState()

    data class Error(val errorCode: Int) : ScanningState()

    data class DevicesDiscovered(val devices: List<DiscoveredBluetoothDevice>) : ScanningState() {
        fun isEmpty(): Boolean = devices.isEmpty()
    }

    fun isRunning(): Boolean {
        return this is Loading || this is DevicesDiscovered
    }
}