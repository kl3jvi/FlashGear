package com.kl3jvi.yonda.manager

import android.bluetooth.BluetoothDevice
import com.kl3jvi.nb_api.command.ScooterCommand
import com.kl3jvi.yonda.manager.state.ConnectionState
import com.kl3jvi.yonda.models.DiscoveredBluetoothDevice
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.Flow

interface ConnectionService {
    val connectionState: ReceiveChannel<ConnectionState>

    fun scanBleDevices(): Flow<DiscoveredBluetoothDevice>
    fun sendCommand(peripheral: BluetoothDevice, scooterCommand: ScooterCommand)
    fun connectToPeripheral(peripheral: BluetoothDevice, callback: (BluetoothDevice) -> Unit)
    fun disconnect(peripheral: BluetoothDevice)
    suspend fun readFromScooter()
    fun startScanning()
    fun stopScanning()
}

interface FlashGearBleManager {

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
}
