package com.kl3jvi.yonda.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kl3jvi.yonda.connectivity.ConnectionService
import com.kl3jvi.yonda.models.BleDevice
import com.welie.blessed.BluetoothPeripheral
import com.welie.blessed.ConnectionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.launch

class HomeViewModel(
    private val connectionService: ConnectionService
) : ViewModel(), ConnectionListener {

    private var bluetoothDevices: Set<BleDevice> = emptySet()
    val currentConnectState = connectionService.currentConnectState().stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        Pair(null, null)
    )

    val scannedDeviceList: Flow<BluetoothState> =
        currentConnectState.flatMapLatest { (peripheralConnected, connectionState) ->
            callbackFlow {
                trySend(BluetoothState.Idle)
                connectionService.scanBleDevices(
                    onSuccess = { bluetoothPeripheral ->
                        bluetoothDevices += BleDevice(bluetoothPeripheral)
                        val listOfDevices = bluetoothDevices.toList().map {
                            if (it.peripheral == peripheralConnected)
                                it.connectionState = connectionState ?: ConnectionState.DISCONNECTED
                            it
                        }
                        trySend(BluetoothState.Success(listOfDevices))
                    },
                    onError = { errorMessage -> trySend(BluetoothState.Error(errorMessage)) }
                )
                awaitClose(::stopScanPressed)
            }
        }.distinctUntilChanged()
            .onEach {
                delay(1000)
            }.takeWhile {
                connectionService.isScanning()
            }.flowOn(Dispatchers.Default)


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
    override fun connect(peripheral: BluetoothPeripheral) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.e("Clicked", "pizza")
            connectionService.connectPeripheral(peripheral)
                .onSuccess {
                    Log.e("Connecting to ${peripheral.name} at", "${System.currentTimeMillis()}")
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
