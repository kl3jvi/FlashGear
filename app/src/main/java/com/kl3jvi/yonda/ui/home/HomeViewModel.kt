package com.kl3jvi.yonda.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kl3jvi.nb_api.command.battery.Battery
import com.kl3jvi.yonda.connectivity.ConnectionService
import com.kl3jvi.yonda.ext.Result
import com.kl3jvi.yonda.ext.aggregateAsSet
import com.kl3jvi.yonda.ext.convertToResultAndMapTo
import com.kl3jvi.yonda.ext.delayEachFor
import com.kl3jvi.yonda.models.ScanHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    val connectionService: ConnectionService,
) : ViewModel(), ConnectListener {

    private val currentConnectivityState = connectionService.connectionState.receiveAsFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            currentConnectivityState.collectLatest {
                Log.e("State", it.toString())
            }

        }

        viewModelScope.launch(Dispatchers.IO) {
            delay(4000)
        }
    }

    var scannedDeviceList: Flow<BluetoothState> = connectionService.scanBleDevices()
        .aggregateAsSet()
        .convertToResultAndMapTo { result ->
            when (result) {
                is Result.Error -> BluetoothState.Error(result.exception?.localizedMessage ?: "")
                Result.Loading -> BluetoothState.Idle
                is Result.Success -> BluetoothState.Success(result.data.toSet())
            }
        }
        .delayEachFor(1_000)
        .flowOn(Dispatchers.IO)

    /**
     * The function stops the scanning process and clears the list of scanned devices
     */
    private fun stopScanPressed() {
        connectionService.stopScanning()
    }

    override fun connectToPeripheral(peripheral: ScanHolder) {
        viewModelScope.launch(Dispatchers.IO) {
            stopScanPressed()
            val device = peripheral.device ?: return@launch
            connectionService.connectToPeripheral(device) {
                Log.i("Connected", "to ${it.address}")
                stopScanPressed()
            }
        }
    }

    override fun sendCommandToPeripheral(peripheral: ScanHolder) {
        Log.e("CLicked ", "sendCommand")
        viewModelScope.launch(Dispatchers.IO) {
//            connectionService.sendCommand(peripheral.device ?: return@launch,Battery())
        }
    }
}


sealed interface BluetoothState {
    data class Success(val data: Set<ScanHolder>) : BluetoothState
    data class Error(val errorMessage: String) : BluetoothState
    object Idle : BluetoothState
}
