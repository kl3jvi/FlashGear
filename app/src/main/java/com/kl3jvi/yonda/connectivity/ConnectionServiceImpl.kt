package com.kl3jvi.yonda.connectivity

import android.bluetooth.le.ScanResult
import com.kl3jvi.yonda.ext.Error
import com.welie.blessed.BluetoothCentralManager
import com.welie.blessed.BluetoothPeripheral
import com.welie.blessed.ConnectionState
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.UUID

class ConnectionServiceImpl : ConnectionService, KoinComponent {
    private val central: BluetoothCentralManager by inject()

    init {
        central.enableLogging()
    }

    override fun scanBleDevices(): Flow<BluetoothPeripheral> = callbackFlow {
        runCatching {
            central.scanForPeripherals(
                resultCallback = { bluetoothPeripheral: BluetoothPeripheral, _: ScanResult ->
                    trySend(bluetoothPeripheral)
                },
                scanError = {
                    cancel(Error(scanFailure = it).getErrorMessage())
                }
            )
        }.onFailure {
            cancel(Error(throwable = it).getErrorMessage())
        }
        awaitClose()
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

    override fun isScanning() = central.isScanning

    override fun isScanningFlow() = flow {
        while (true) {
            delay(1000)
            emit(isScanning())
        }
    }

    override fun stopScanning() = central.stopScan()

    override fun currentConnectState() = callbackFlow {
        central.observeConnectionState { peripheral, state ->
            trySend(peripheral to state)
        }
        awaitClose()
    }

    override fun connectToPeripheral(peripheral: BluetoothPeripheral): Flow<Pair<BluetoothPeripheral, ConnectionState>> =
        callbackFlow {
            runCatching {
                central.connectPeripheral(peripheral)
            }.onFailure {
                cancel(it.message ?: "Error Connecting!")
            }
            central.observeConnectionState { peripheral, state ->
                trySend(peripheral to state)
            }
            awaitClose()
        }

    override suspend fun readFromScooter(peripheral: BluetoothPeripheral): ByteArray {
        return peripheral.readCharacteristic(CHAR_READ, CHAR_WRITE)
    }

    companion object {
        val CHAR_WRITE: UUID = UUID.fromString("6e400002-b5a3-f393-e0a9-e50e24dcca9e") // WRITE
        val CHAR_READ: UUID = UUID.fromString("6e400003-b5a3-f393-e0a9-e50e24dcca9e") // READ
    }
}
