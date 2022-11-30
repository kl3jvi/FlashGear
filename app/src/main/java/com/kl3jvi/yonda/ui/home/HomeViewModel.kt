package com.kl3jvi.yonda.ui.home

import android.bluetooth.le.ScanResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kl3jvi.yonda.connectivity.ConnectionService
import com.welie.blessed.BluetoothPeripheral
import com.welie.blessed.ScanFailure
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(
    private val connectionService: ConnectionService,

) : ViewModel() {
    val connectedDevices = callbackFlow {
        trySend(BluetoothState.Idle)
        connectionService.scanBleDevices(
            onSuccess = {
                trySend(BluetoothState.Success(it))
            },
            onError = {
                trySend(BluetoothState.Error(it))
            }
        )
        awaitClose(::stopScanPressed)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        BluetoothState.Idle
    )

    fun stopScanPressed() =connectionService.stopScanning()


}

sealed interface BluetoothState {
    data class Success(val data: Pair<BluetoothPeripheral, ScanResult>) : BluetoothState
    data class Error(val exception: ScanFailure) : BluetoothState
    object Idle : BluetoothState
}




