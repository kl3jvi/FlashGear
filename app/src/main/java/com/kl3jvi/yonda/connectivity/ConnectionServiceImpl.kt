package com.kl3jvi.yonda.connectivity

import android.bluetooth.le.ScanResult
import com.kl3jvi.yonda.ext.Error
import com.welie.blessed.BluetoothCentralManager
import com.welie.blessed.BluetoothPeripheral
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ConnectionServiceImpl : ConnectionService, KoinComponent {
    private val central: BluetoothCentralManager by inject()

    init {
        central.enableLogging()
    }

    override fun scanBleDevices(
        onSuccess: (BluetoothPeripheral) -> Unit,
        onError: (String) -> Unit
    ) {
        runCatching {
            central.scanForPeripherals(
                resultCallback = { bluetoothPeripheral: BluetoothPeripheral, _: ScanResult ->
                    onSuccess(bluetoothPeripheral)
                },
                scanError = {
                    onError(Error(scanFailure = it).getErrorMessage())
                }
            )
        }.onFailure {
            onError(Error(throwable = it).getErrorMessage())
        }
    }

    override fun isBluetoothEnabled() = central.isBluetoothEnabled

    override fun stopScanning() = central.stopScan()

    override fun isScanning() = central.isScanning


    override fun currentConnectState() = callbackFlow {
        central.observeConnectionState { peripheral, state ->
            trySend(peripheral to state)
        }
        awaitClose { central.close() }
    }

    private fun checkPeripheralIsConnected(peripheral: BluetoothPeripheral): Boolean {
        return central.getConnectedPeripherals().contains(peripheral)
    }

    override suspend fun connectPeripheral(bluetoothPeripheral: BluetoothPeripheral) =
        withContext(Dispatchers.IO) {
            runCatching { central.connectPeripheral(bluetoothPeripheral) }
        }
}





