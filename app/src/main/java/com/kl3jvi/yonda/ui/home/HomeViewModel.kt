package com.kl3jvi.yonda.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kl3jvi.yonda.connectivity.ConnectionService
import com.kl3jvi.yonda.models.BleDevice
import com.welie.blessed.BluetoothPeripheral
import com.welie.blessed.ScanFailure
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val connectionService: ConnectionService
) : ViewModel(), ConnectionListener {
    private var bluetoothDevices: Set<BleDevice> = emptySet()
    val connectedDevices = callbackFlow {
        trySend(BluetoothState.Idle)
        connectionService.scanBleDevices(
            onSuccess = { bluetoothPheripheral ->
                bluetoothDevices += BleDevice(bluetoothPheripheral)
                trySend(BluetoothState.Success(bluetoothDevices.toList()))
            },
            onError = { scanFailure, throwable ->
                trySend(BluetoothState.Error(scanFailure = scanFailure, exception = throwable))
            }
        )
        awaitClose(::stopScanPressed)
    }

    fun stopScanPressed() = connectionService.stopScanning()

    /**
     * The function connects to a Bluetooth peripheral using the connection service
     *
     * @param pheripheral BluetoothPeripheral - The peripheral to connect to.
     */
    override fun connect(pheripheral: BluetoothPeripheral) {
        viewModelScope.launch(Dispatchers.IO) {
            connectionService.connectPeripheral(pheripheral)
                .onSuccess {
                    Log.e("Connecting to ${pheripheral.name} at", "${System.currentTimeMillis()}")
                }.onFailure {
                    Log.e("Error occurred", "when connecting", it)
                }
        }
    }
}

sealed interface BluetoothState {
    data class Success(val data: List<BleDevice>) : BluetoothState

    data class Error(
        val scanFailure: ScanFailure? = null,
        val exception: Throwable? = null
    ) : BluetoothState

    object Idle : BluetoothState
}
