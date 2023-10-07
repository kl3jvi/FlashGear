package com.kl3jvi.yonda.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kl3jvi.nb_api.command.locking.LockOff
import com.kl3jvi.yonda.ext.Result
import com.kl3jvi.yonda.ext.aggregateAsSet
import com.kl3jvi.yonda.ext.convertToResultAndMapTo
import com.kl3jvi.yonda.ext.delayEachFor
import com.kl3jvi.yonda.manager.ConnectionService
import com.kl3jvi.yonda.models.ScanHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val connectionService: ConnectionService,
) : ViewModel(), ConnectListener {

    val currentConnectivityState = connectionService
        .connectionState
        .receiveAsFlow()

    private val scanCommands = Channel<ScanCommand>(1, BufferOverflow.DROP_OLDEST)
    fun commands(): Flow<ScanCommand> = scanCommands.receiveAsFlow()

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

    fun sendCommand(newCommand: ScanCommand) {
        viewModelScope.launch {
            scanCommands.send(newCommand)
        }
    }

    fun startScanning() = connectionService.startScanning()
    fun stopScanning() = connectionService.stopScanning()

    override fun connectToPeripheral(peripheral: ScanHolder) {
        viewModelScope.launch(Dispatchers.IO) {
            peripheral.device?.let { device ->
                connectionService.connectToPeripheral(device) {
                    stopScanning()
                }
            }
        }
    }

    override fun sendCommandToPeripheral(peripheral: ScanHolder) {
        viewModelScope.launch(Dispatchers.IO) {
            // Add your logic here if needed
            peripheral.device?.let { device ->
                connectionService.sendCommand(device, LockOff())
            }
        }
    }
}

sealed interface BluetoothState {
    data class Success(val data: Set<ScanHolder>) : BluetoothState
    data class Error(val errorMessage: String) : BluetoothState
    object Idle : BluetoothState
}

sealed class ScanCommand {
    object Start : ScanCommand()
    object Stop : ScanCommand()
}
