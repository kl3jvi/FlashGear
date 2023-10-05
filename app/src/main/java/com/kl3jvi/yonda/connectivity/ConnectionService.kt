package com.kl3jvi.yonda.connectivity

import android.bluetooth.BluetoothDevice
import com.kl3jvi.nb_api.command.ScooterCommand
import com.kl3jvi.yonda.models.ScanHolder
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow

interface ConnectionService {
    val connectionState: Channel<ConnectionState>

    fun scanBleDevices(): Flow<ScanHolder>
    fun startScanning()
    fun stopScanning()
     fun sendCommand(peripheral: BluetoothDevice, scooterCommand: ScooterCommand)
    suspend fun readFromScooter()
    fun connectToPeripheral(peripheral: BluetoothDevice, callback: (BluetoothDevice) -> Unit)
    fun disconnect(peripheral: BluetoothDevice, callback: (BluetoothDevice) -> Unit)
}
