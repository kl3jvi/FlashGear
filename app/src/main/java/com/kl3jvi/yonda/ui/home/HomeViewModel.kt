package com.kl3jvi.yonda.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kl3jvi.yonda.connectivity.ConnectionService
import com.kl3jvi.yonda.ext.Result
import com.kl3jvi.yonda.ext.convertToResultAndMapTo
import com.kl3jvi.yonda.ext.delayEachFor
import com.kl3jvi.yonda.models.BleDevice
import com.welie.blessed.BluetoothPeripheral
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val connectionService: ConnectionService
) : ViewModel(), ConnectListener {

    private var bluetoothDevices: MutableSet<BleDevice> = mutableSetOf()
    var checkForAnimation = MutableStateFlow(false)

    var scannedDeviceList: Flow<BluetoothState> = connectionService.scanBleDevices()
        .convertToResultAndMapTo { result ->
            when (result) {
                is Result.Error -> BluetoothState.Error(result.exception?.localizedMessage ?: "")
                Result.Loading -> BluetoothState.Idle
                is Result.Success -> {
                    bluetoothDevices.add(BleDevice(result.data))
                    BluetoothState.Success(bluetoothDevices.toList())
                }
            }
        }
        .distinctUntilChanged()
        .delayEachFor(1000)
        .flowOn(Dispatchers.IO)

    /**
     * The function stops the scanning process and clears the list of scanned devices
     */
    fun stopScanPressed() {
        connectionService.stopScanning()
        scannedDeviceList = scannedDeviceList.drop(bluetoothDevices.size)
        bluetoothDevices = mutableSetOf()
    }

    /**
     * The function connects to a Bluetooth peripheral using the connection service
     *
     * @param peripheral BluetoothPeripheral - The peripheral to connect to.
     */
    override fun connectToPeripheral(peripheral: BluetoothPeripheral) {
        viewModelScope.launch(Dispatchers.IO) {
            stopScanPressed()
            connectionService.connectToPeripheral(peripheral).collect {
                Log.e("Device-${it.first.address}", "is ${it.second}")
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
