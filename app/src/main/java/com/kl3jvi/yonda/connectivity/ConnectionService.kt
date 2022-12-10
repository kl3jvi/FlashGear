package com.kl3jvi.yonda.connectivity

import com.welie.blessed.BluetoothPeripheral
import com.welie.blessed.ConnectionState
import kotlinx.coroutines.flow.Flow

interface ConnectionService {

    /**
     * "Scan for BLE devices and call the onSuccess function when a device is found, or call the
     * onError function if there's an error."
     *
     *
     * @param onSuccess This is a callback that will be called when a device is found. It will be
     * called with a Pair of BluetoothPeripheral and ScanResult.
     * @param onError (ScanFailure) -> Unit
     */
    fun scanBleDevices(
        onSuccess: (BluetoothPeripheral) -> Unit,
        onError: (String) -> Unit
    )

    /**
     * Returns true if Bluetooth is enabled, false otherwise.
     */
     fun isBluetoothEnabled(): Flow<Boolean>

    /**
     * Stop scanning for peripherals
     */
    fun stopScanning()

    /**
     * It returns a boolean value.
     */
    fun isScanning(): Flow<Boolean>

    /**
     * This function connects to a Bluetooth peripheral
     *
     * @param bluetoothPeripheral The BluetoothPeripheral object that you want to connect to.
     */
    suspend fun connectPeripheral(bluetoothPeripheral: BluetoothPeripheral): Result<Unit>

    /**
     * "This function returns a Flow of ConnectionState objects."
     *
     * The Flow class is a Kotlin class that is part of the Kotlin Coroutines library. It's a class
     * that represents a stream of data
     */
    fun currentConnectState(): Flow<Pair<BluetoothPeripheral, ConnectionState>>
    suspend fun readFromScooter(peripheral: BluetoothPeripheral): ByteArray
    val isScanning: Boolean
}
