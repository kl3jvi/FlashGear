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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val connectionService: ConnectionService
) : ViewModel(), ConnectListener {

    var checkForAnimation = MutableStateFlow(false)
    val currentConnectivityState = connectionService.connectionState.asStateFlow()

    var scannedDeviceList: Flow<BluetoothState> = connectionService.scanBleDevices()
        .scan(emptySet<BluetoothPeripheral>()) { acc, value -> acc.plus(value) }
        .convertToResultAndMapTo { result ->
            when (result) {
                is Result.Error -> {
                    Log.e("Error retrying", "scan", result.exception)
                    BluetoothState.Error(result.exception?.localizedMessage ?: "")
                }

                Result.Loading -> BluetoothState.Idle
                is Result.Success -> {
                    val listOfDevices = result.data.map(::BleDevice)
                    BluetoothState.Success(listOfDevices)
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
        scannedDeviceList
        checkForAnimation.update { false }
    }

    /**
     * The function connects to a Bluetooth peripheral using the connection service
     *
     * @param peripheral BluetoothPeripheral - The peripheral to connect to.
     */
    override fun connectToPeripheral(peripheral: BluetoothPeripheral) {
        viewModelScope.launch(Dispatchers.IO) {
            stopScanPressed()
            connectionService.connectToPeripheral(peripheral)
        }
    }

    override fun sendCommandToPeripheral(peripheral: BluetoothPeripheral) {
        Log.e("CLicked ", "sendCimmand")
        viewModelScope.launch(Dispatchers.IO) {
            connectionService.readFromScooter(peripheral)
        }
    }
}

private suspend fun <T> Flow<T>.dropAll(): Flow<T> {
    val size = toList().size
    return drop(size)
}

sealed interface BluetoothState {
    data class Success(val data: List<BleDevice>) : BluetoothState
    data class Error(val errorMessage: String) : BluetoothState
    object Idle : BluetoothState
}
