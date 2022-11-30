package com.kl3jvi.yonda.ui.home

import androidx.lifecycle.ViewModel
import com.kl3jvi.yonda.connectivity.ConnectionService
import com.welie.blessed.ScanFailure
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class HomeViewModel(
    private val connectionService: ConnectionService
) : ViewModel() {
    private var bluetoothDevices: Set<BleDevice> = emptySet()
    val connectedDevices = callbackFlow {
        trySend(BluetoothState.Idle)
        connectionService.scanBleDevices(
            onSuccess = {
                bluetoothDevices += BleDevice(it.first.name, it.first.address)
                trySend(BluetoothState.Success(bluetoothDevices.toList()))
            },
            onLibraryError = {
                trySend(BluetoothState.Error(scanFailure = it))
            },
            onError = {
                trySend(BluetoothState.Error(exception = it))
            }
        )
        awaitClose(::stopScanPressed)
    }

    fun stopScanPressed() = connectionService.stopScanning()
}

sealed interface BluetoothState {
    data class Success(val data: List<BleDevice>) : BluetoothState
    data class Error(
        val scanFailure: ScanFailure? = null,
        val exception: Throwable? = null
    ) : BluetoothState

    object Idle : BluetoothState
}

data class BleDevice(
    val name: String,
    val macAddress: String
)