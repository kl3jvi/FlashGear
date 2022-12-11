package com.kl3jvi.yonda.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kl3jvi.yonda.connectivity.ConnectionService
import com.kl3jvi.yonda.ext.Result
import com.kl3jvi.yonda.ext.asResult
import com.kl3jvi.yonda.models.BleDevice
import com.welie.blessed.BluetoothPeripheral
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.launch

class HomeViewModel(
    private val connectionService: ConnectionService
) : ViewModel(), ConnectListener {

    private var bluetoothDevices: Set<BleDevice> = emptySet()

    val connState = connectionService.currentConnectState()
    val isScanning = connectionService.isScanning()
    val isBleEnabled = connectionService.isBluetoothEnabled()

    val scannedDeviceList: Flow<BluetoothState> = connectionService.scanBleDevices()
        .asResult()
        .map {
            when (it) {
                is Result.Error -> BluetoothState.Error(it.exception?.localizedMessage ?: "")
                Result.Loading -> BluetoothState.Idle
                is Result.Success -> {
                    bluetoothDevices += BleDevice(it.data)
                    BluetoothState.Success(bluetoothDevices.toList())
                }
            }
        }
        .distinctUntilChanged()
        .onEach {
            delay(1000)
        }.takeWhile {
            connectionService.isScanning
        }.flowOn(Dispatchers.Default)

    /**
     * The function stops the scanning process and clears the list of scanned devices
     */
    fun stopScanPressed() {
        connectionService.stopScanning()
        scannedDeviceList.drop(bluetoothDevices.size)
        bluetoothDevices = emptySet()
    }

    /**
     * The function connects to a Bluetooth peripheral using the connection service
     *
     * @param peripheral BluetoothPeripheral - The peripheral to connect to.
     */
    override fun connectToPeripheral(peripheral: BluetoothPeripheral) {
        viewModelScope.launch(Dispatchers.IO) {
            stopScanPressed()
            connectionService.connectPeripheral(peripheral)
                .onSuccess {
                    Log.e(
                        "Connecting to ${peripheral.name} at",
                        "${System.currentTimeMillis()}"
                    )
                }.onFailure {
                    Log.e("Error occurred", "when connecting", it)
                    return@launch
                }
        }
    }
}

sealed interface BluetoothState {
    data class Success(
        val data: List<BleDevice>
    ) : BluetoothState

    data class Error(val errorMessage: String) : BluetoothState
    object Idle : BluetoothState
}
