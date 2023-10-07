package com.kl3jvi.yonda.manager

import android.bluetooth.BluetoothDevice
import com.kl3jvi.nb_api.command.ScooterCommand
import com.kl3jvi.yonda.manager.state.ConnectionState
import com.kl3jvi.yonda.models.ScanHolder
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.Flow

interface ConnectionService {
    val connectionState: ReceiveChannel<ConnectionState>

    fun startScanning()
    fun scanBleDevices(): Flow<ScanHolder>
    fun stopScanning()
    fun sendCommand(peripheral: BluetoothDevice, scooterCommand: ScooterCommand)
    suspend fun readFromScooter()
    fun connectToPeripheral(peripheral: BluetoothDevice, callback: (BluetoothDevice) -> Unit)
    fun disconnect(peripheral: BluetoothDevice)
}

interface YondaBleManager {

    fun isConnected(): Boolean

    /**
     * Connect to device {@param device}
     * Await while disconnect process is not finish
     */
    suspend fun connectToDevice(device: BluetoothDevice)

    /**
     * Disconnect from current device
     * Await while disconnect process is not finish
     */
    suspend fun disconnectDevice()

    /**
     * Close manager, unregister receivers
     */
    fun close()
}
