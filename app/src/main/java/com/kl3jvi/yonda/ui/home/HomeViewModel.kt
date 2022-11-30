package com.kl3jvi.yonda.ui.home

import android.bluetooth.le.ScanResult
import androidx.lifecycle.ViewModel
import com.kl3jvi.yonda.connectivity.ConnectionService
import com.welie.blessed.BluetoothPeripheral
import com.welie.blessed.ScanFailure
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class HomeViewModel(
    private val connectionService: ConnectionService
) : ViewModel() {
    val connectedDevices = callbackFlow {
        trySend(BluetoothState.Idle)
        connectionService.scanBleDevices(
            onSuccess = {
                trySend(BluetoothState.Success(it))
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
    data class Success(val data: Pair<BluetoothPeripheral, ScanResult>) : BluetoothState
    data class Error(
        val scanFailure: ScanFailure? = null,
        val exception: Throwable? = null
    ) : BluetoothState

    object Idle : BluetoothState
}
