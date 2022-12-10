package com.kl3jvi.yonda.connectivity

import android.bluetooth.le.ScanResult
import com.kl3jvi.yonda.ext.Error
import com.welie.blessed.BluetoothCentralManager
import com.welie.blessed.BluetoothPeripheral
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import java.util.UUID

class ConnectionServiceImpl(
    private val central: BluetoothCentralManager
) : ConnectionService, KoinComponent {

    init {
        central.enableLogging()
    }

    override fun scanBleDevices(
        onSuccess: (BluetoothPeripheral) -> Unit, onError: (String) -> Unit
    ) {
        runCatching {
            central.scanForPeripherals(resultCallback = { bluetoothPeripheral: BluetoothPeripheral, _: ScanResult ->
                onSuccess(bluetoothPeripheral)
            }, scanError = {
                onError(Error(scanFailure = it).getErrorMessage())
            })
        }.onFailure {
            onError(Error(throwable = it).getErrorMessage())
        }
    }

    /**
     * > This function returns a flow that emits the current state of bluetooth every second
     */
    override fun isBluetoothEnabled() = flow {
        while (true) {
            delay(1000)
            emit(central.isBluetoothEnabled)
        }
    }

    override fun isScanning() = flow {
        while (true) {
            delay(1000)
            emit(isScanning)
        }
    }

    override val isScanning = central.isScanning


    override fun stopScanning() = central.stopScan()


    override fun currentConnectState() = callbackFlow {
        central.observeConnectionState { peripheral, state ->
            trySend(peripheral to state)
        }
        awaitClose { central.close() }
    }

    override suspend fun connectPeripheral(bluetoothPeripheral: BluetoothPeripheral) =
        withContext(Dispatchers.IO) {
            runCatching { central.connectPeripheral(bluetoothPeripheral) }
        }

    override suspend fun readFromScooter(peripheral: BluetoothPeripheral): ByteArray {
        return peripheral.readCharacteristic(CHAR_READ, CHAR_WRITE)
    }

    companion object {
        val CHAR_WRITE: UUID = UUID.fromString("6e400002-b5a3-f393-e0a9-e50e24dcca9e") //WRITE
        val CHAR_READ: UUID = UUID.fromString("6e400003-b5a3-f393-e0a9-e50e24dcca9e") //READ
    }
}