package com.kl3jvi.yonda.connectivity

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.content.Context
import android.os.ParcelUuid
import android.util.Log
import com.kl3jvi.yonda.connectivity.MyBleManager.Companion.XIAOMI_PARCEL_UUID
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import no.nordicsemi.android.ble.BleManager
import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat
import no.nordicsemi.android.support.v18.scanner.ScanCallback
import no.nordicsemi.android.support.v18.scanner.ScanFilter
import no.nordicsemi.android.support.v18.scanner.ScanResult
import no.nordicsemi.android.support.v18.scanner.ScanSettings
import java.util.UUID


class NordicBleConnectionService(
    private val context: Context
) : ConnectionService, ScanCallback() {

    override val connectionState: Channel<ConnectionState>
        get() = Channel(1, BufferOverflow.DROP_OLDEST)


    override fun scanBleDevices(): Flow<BluetoothDevice> = flow {
        val scanner = BluetoothLeScannerCompat.getScanner()

        val settings: ScanSettings = ScanSettings.Builder()
            .setLegacy(false)
            .setScanMode(ScanSettings.SCAN_MODE_BALANCED)
            .setUseHardwareBatchingIfSupported(true)
            .setReportDelay(5000)
            .build()
        val filters: MutableList<ScanFilter> = ArrayList()

        filters.add(
            ScanFilter.Builder()
                .setServiceUuid(XIAOMI_PARCEL_UUID)
                .build()
        )

        scanner.startScan(filters, settings, this@NordicBleConnectionService)
    }

    override fun onScanResult(callbackType: Int, result: ScanResult) {
        super.onScanResult(callbackType, result)
        Log.e("result", result.toString())

    }

    override fun onBatchScanResults(results: MutableList<ScanResult>) {
        super.onBatchScanResults(results)
        Log.e("results", results.toString())
    }

    override fun onScanFailed(errorCode: Int) {
        super.onScanFailed(errorCode)
        val throwable = Throwable("Scan failed with error code $errorCode")
        throwable.printStackTrace()
        Log.e("error", throwable.toString(), throwable)
    }


    override fun stopScanning() {
        connectionState.trySend(ConnectionState.Disconnecting).isSuccess
        // Stop scanning
        val scanner = BluetoothLeScannerCompat.getScanner()
        scanner.stopScan(this)
        connectionState.trySend(ConnectionState.Disconnected).isSuccess
    }

    override fun connectToPeripheral(
        peripheral: BluetoothDevice,
        callback: (BluetoothDevice) -> Unit
    ) {
        MyBleManager(context)
            .connect(peripheral)
            .timeout(10000) // adjust as needed
            .retry(3, 100)
            .before {
                connectionState.trySend(ConnectionState.Connecting).isSuccess
            }
            .done {
                connectionState.trySend(ConnectionState.Connected).isSuccess
                callback(it)
            }
            .fail { device, status ->
                connectionState.trySend(ConnectionState.Failed(device, status)).isSuccess
            }
            .enqueue()
    }

    override suspend fun sendCommand(peripheral: BluetoothDevice): ByteArray {
        // This largely depends on how you've set up MyBleManager and the GATT services/characteristics you're communicating with.
        return byteArrayOf() // Placeholder
    }

    override suspend fun readFromScooter(peripheral: BluetoothDevice) {
        // Similar to sendCommand(), this is dependent on your GATT setup
    }


}

class MyBleManager(context: Context) : BleManager(context) {

    private var commandCharacteristic: BluetoothGattCharacteristic? = null
    private var readDataCharacteristic: BluetoothGattCharacteristic? = null

    override fun log(priority: Int, message: String) {
        when (priority) {
            Log.ASSERT -> Log.e("NordicBleManager", message)
            Log.DEBUG -> Log.d("NordicBleManager", message)
            Log.ERROR -> Log.e("NordicBleManager", message)
            Log.INFO -> Log.i("NordicBleManager", message)
            Log.VERBOSE -> Log.v("NordicBleManager", message)
            Log.WARN -> Log.w("NordicBleManager", message)
        }
    }


    override fun getGattCallback(): BleManagerGattCallback {
        return MyBleManagerGattCallback()
    }

    private inner class MyBleManagerGattCallback : BleManagerGattCallback() {

        // This method will be called when the device is connected and services are discovered.
        // You should return true if all required services and characteristics are found.
        // If the device does not have the required services, return false to disconnect from the device.
        override fun isRequiredServiceSupported(gatt: BluetoothGatt): Boolean {
            val scooterService = gatt.getService(XIAOMI_SERVICE)
            scooterService?.let {
                commandCharacteristic = it.getCharacteristic(CHAR_WRITE)
                readDataCharacteristic = it.getCharacteristic(CHAR_READ)
            }
            return commandCharacteristic != null && readDataCharacteristic != null
        }

        // Initialization after services are discovered.
        // Here you can enable notifications for specific characteristics for example.
        override fun initialize() {
            // Enable notifications for readDataCharacteristic for example.
            beginAtomicRequestQueue()
                .add(enableNotifications(readDataCharacteristic))
                .enqueue()
        }

        override fun onServicesInvalidated() {
            TODO("Not yet implemented")
        }

        // This method should return true if the device still has the required services after a bond is lost, for example.
        fun areServicesValid(): Boolean {
            return commandCharacteristic != null && readDataCharacteristic != null
        }


        // Handle characteristic updates
        override fun onCharacteristicNotified(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic
        ) {
            if (characteristic == readDataCharacteristic) {
                // handle new data
            }
        }
    }

    companion object {
        val XIAOMI_SERVICE: UUID =
            UUID.fromString("6e400001-b5a3-f393-e0a9-e50e24dcca9e")
        val XIAOMI_PARCEL_UUID: ParcelUuid = ParcelUuid(XIAOMI_SERVICE)
        val CHAR_WRITE: UUID = UUID.fromString("6e400002-b5a3-f393-e0a9-e50e24dcca9e")
        val CHAR_READ: UUID = UUID.fromString("6e400003-b5a3-f393-e0a9-e50e24dcca9e")
    }


}