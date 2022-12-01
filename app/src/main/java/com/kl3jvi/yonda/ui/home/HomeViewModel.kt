package com.kl3jvi.yonda.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kl3jvi.yonda.connectivity.ConnectionService
import com.kl3jvi.yonda.models.BleDevice
import com.welie.blessed.BluetoothPeripheral
import com.welie.blessed.asString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.launch
import java.util.UUID

class HomeViewModel(
    private val connectionService: ConnectionService
) : ViewModel(), ConnectionListener {

    private var bluetoothDevices: Set<BleDevice> = emptySet()
    var scanStopped = false

    val connectedDevices = callbackFlow {
        trySend(BluetoothState.Idle)
        connectionService.scanBleDevices(
            onSuccess = { bluetoothPeripheral ->
                bluetoothDevices += BleDevice(bluetoothPeripheral)
                trySend(
                    BluetoothState.Success(
                        bluetoothDevices.toList()
                    )
                )
            },
            onError = { errorMessage -> trySend(BluetoothState.Error(errorMessage)) }
        )

        awaitClose(::stopScanPressed)
    }.distinctUntilChanged()
        .onEach {
            delay(1000)
        }.takeWhile {
            connectionService.isScanning()
        }.flowOn(Dispatchers.Default)


    fun stopScanPressed() {
        connectionService.stopScanning()
    }

    /**
     * The function connects to a Bluetooth peripheral using the connection service
     *
     * @param peripheral BluetoothPeripheral - The peripheral to connect to.
     */
    override fun connect(peripheral: BluetoothPeripheral) {
        viewModelScope.launch(Dispatchers.IO) {
            connectionService.connectPeripheral(peripheral)
                .onSuccess {
                    Log.e("Connecting to ${peripheral.name} at", "${System.currentTimeMillis()}")
                }.onFailure {
                    Log.e("Error occurred", "when connecting", it)
                    return@launch
                }
            peripheral.getCharacteristic(UUID.randomUUID(), UUID.randomUUID())?.let {
                val manufacturerName = peripheral.readCharacteristic(it).asString()
                Log.e("Received:", manufacturerName)
            }
        }
    }
}

sealed interface BluetoothState {
    data class Success(val data: List<BleDevice>) : BluetoothState
    data class Error(val errorMessage: String) : BluetoothState
    object Idle : BluetoothState
}
