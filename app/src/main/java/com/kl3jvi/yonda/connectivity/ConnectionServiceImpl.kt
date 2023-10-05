package com.kl3jvi.yonda.connectivity

import android.bluetooth.BluetoothDevice
import android.os.ParcelUuid
import android.util.Log
import com.kl3jvi.nb_api.command.ScooterCommand
import com.kl3jvi.nb_api.command.battery.Battery
import com.kl3jvi.nb_api.command.version.CheckVersion
import com.kl3jvi.yonda.models.ScanHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import no.nordicsemi.android.ble.observer.ConnectionObserver
import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat
import no.nordicsemi.android.support.v18.scanner.ScanCallback
import no.nordicsemi.android.support.v18.scanner.ScanFilter
import no.nordicsemi.android.support.v18.scanner.ScanResult
import no.nordicsemi.android.support.v18.scanner.ScanSettings
import java.util.UUID


class ConnectionServiceImpl(
    private val bleManager: FlashGearBluetoothManager
) : ConnectionService {

    override val connectionState: Channel<ConnectionState>
        get() = Channel(1, BufferOverflow.DROP_OLDEST)
    private val scanCommands: Channel<ScanCommand> = Channel(1, BufferOverflow.DROP_OLDEST)


    sealed class ScanCommand {
        object Start : ScanCommand()
        object Stop : ScanCommand()
    }

    override fun startScanning() {
        scanCommands.trySend(ScanCommand.Start)
    }

    override fun stopScanning() {
        scanCommands.trySend(ScanCommand.Stop)
    }

    override fun scanBleDevices(): Flow<ScanHolder> = callbackFlow {
        val scanner = BluetoothLeScannerCompat.getScanner()

        val settings: ScanSettings = ScanSettings.Builder()
            .setLegacy(false)
            .setScanMode(ScanSettings.SCAN_MODE_BALANCED)
            .setUseHardwareBatchingIfSupported(true)
            .setReportDelay(1000)
            .build()

        val filter = listOf<ScanFilter>()

        val callback = object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult) {
                super.onScanResult(callbackType, result)
                trySend(ScanHolder(result.device, result.rssi)).isSuccess
            }

            override fun onBatchScanResults(results: MutableList<ScanResult>) {
                super.onBatchScanResults(results)
                Log.e("Scan", results.toString())
                results.toSet()
                    .forEach { result ->
                        trySend(ScanHolder(result.device, result.rssi)).isSuccess
                    }
            }

            override fun onScanFailed(errorCode: Int) {
                super.onScanFailed(errorCode)
                trySend(ScanHolder(errorCode = errorCode)).isSuccess
            }
        }

        val job = launch {
            scanCommands
                .receiveAsFlow()
                .collectLatest { command ->
                    when (command) {
                        ScanCommand.Start -> {
                            scanner.startScan(filter, settings, callback)
                        }

                        ScanCommand.Stop -> {
                            scanner.stopScan(callback)
                            close()
                        }
                    }
                }
        }

        awaitClose {
            job.cancel()
            scanner.stopScan(callback)
        }
    }


    override fun connectToPeripheral(
        peripheral: BluetoothDevice,
        callback: (BluetoothDevice) -> Unit
    ) {
        bleManager.connectionObserver = object : ConnectionObserver {
            override fun onDeviceConnecting(device: BluetoothDevice) {
                Log.e("Connecting", "Connecting")
                connectionState.trySend(ConnectionState.Connecting).isSuccess
            }

            override fun onDeviceConnected(device: BluetoothDevice) {
                Log.e("Connected", "Connected")
                connectionState.trySend(ConnectionState.Connected).isSuccess
            }

            override fun onDeviceFailedToConnect(device: BluetoothDevice, reason: Int) {
                Log.e("Failed", "Failed")
                connectionState.trySend(ConnectionState.Failed(device, reason)).isSuccess
            }

            override fun onDeviceReady(device: BluetoothDevice) {
                Log.e("Ready", "Ready")
                connectionState.trySend(ConnectionState.Ready(device)).isSuccess
                sendCommand(device, CheckVersion())
            }

            override fun onDeviceDisconnecting(device: BluetoothDevice) {
                Log.e("Disconnecting", "Disconnecting")
                connectionState.trySend(ConnectionState.Disconnecting).isSuccess
            }

            override fun onDeviceDisconnected(device: BluetoothDevice, reason: Int) {
                Log.e("Disconnected", "Disconnected")
                connectionState.trySend(ConnectionState.Disconnected).isSuccess
            }

        }

        bleManager
            .connect(peripheral)
            .useAutoConnect(false)
            .timeout(10000) // adjust as needed
            .retry(3, 100)
            .done {
                callback(it)
            }.fail { device, status ->
                Log.e("Failed Connection", "Failed $device $status")
            }.enqueue()


    }

    override fun disconnect(
        peripheral: BluetoothDevice,
        callback: (BluetoothDevice) -> Unit
    ) {
        bleManager
            .disconnect()
            .enqueue()
    }

    override fun sendCommand(peripheral: BluetoothDevice, scooterCommand: ScooterCommand) {

        CoroutineScope(Dispatchers.IO).launch {
            repeat(3) {
                val result = it * 1000L
                delay(result)
                val command = scooterCommand.getRequestString().toByteArray()
                (bleManager as? FlashGearBluetoothManager)?.sendCommandToDevice(command)
            }
        }
    }


    override suspend fun readFromScooter() {

    }

    companion object {
        val XIAOMI_SERVICE: UUID = UUID.fromString("6e400001-b5a3-f393-e0a9-e50e24dcca9e")
    }
}

