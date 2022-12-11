package com.kl3jvi.yonda.connectivity

import com.welie.blessed.BluetoothPeripheral
import com.welie.blessed.ConnectionState
import kotlinx.coroutines.flow.Flow

interface ConnectionService {

    /**
     * "This function returns a Flow of BluetoothPeripheral objects, which are the devices that are
     * found by the scan."
     */
    fun scanBleDevices(): Flow<BluetoothPeripheral>

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
    fun isScanning(): Boolean

    fun isScanningFlow(): Flow<Boolean>

    /**
     * It returns a Flow of Pairs of BluetoothPeripheral and ConnectionState
     *
     * @param peripheral The peripheral to connect to.
     */
    fun connectToPeripheral(peripheral: BluetoothPeripheral): Flow<Pair<BluetoothPeripheral, ConnectionState>>

    /**
     * "This function returns a Flow of ConnectionState objects."
     *
     * The Flow class is a Kotlin class that is part of the Kotlin Coroutines library. It's a class
     * that represents a stream of data
     */
    fun currentConnectState(): Flow<Pair<BluetoothPeripheral, ConnectionState>>

    /**
     * "Reads the data from the scooter and returns it as a byte array."
     *
     * @param peripheral The BluetoothPeripheral object that you want to read from.
     */
    suspend fun readFromScooter(peripheral: BluetoothPeripheral): ByteArray
}
