package com.kl3jvi.yonda.connectivity

import com.welie.blessed.BluetoothPeripheral
import kotlinx.coroutines.flow.Flow

interface ConnectionService {

    /**
     * "This function returns a Flow of BluetoothPeripheral objects, which are the devices that are
     * found by the scan."
     */
    fun scanBleDevices(): Flow<BluetoothPeripheral>

    /**
     * Stop scanning for peripherals
     */
    fun stopScanning()

    /**
     * It returns a Flow of Pairs of BluetoothPeripheral and ConnectionState
     *
     * @param peripheral The peripheral to connect to.
     */
    fun connectToPeripheral(peripheral: BluetoothPeripheral)

    /**
     * `sendCommand` sends a command to a Bluetooth peripheral and returns the response
     *
     * @param peripheral The BluetoothPeripheral object that you want to send the command to.
     */
    suspend fun sendCommand(peripheral: BluetoothPeripheral): ByteArray

    /**
     * It reads the data from the scooter and returns it as a `ScooterData` object
     *
     * @param peripheral The BluetoothPeripheral object that you want to read from.
     */
    suspend fun readFromScooter(peripheral: BluetoothPeripheral)
}
