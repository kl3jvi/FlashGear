package com.kl3jvi.yonda.connectivity

import android.bluetooth.BluetoothDevice
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow

interface ConnectionService {

    fun scanBleDevices(): Flow<BluetoothDevice>
    fun stopScanning()
    suspend fun sendCommand(peripheral: BluetoothDevice): ByteArray
    suspend fun readFromScooter(peripheral: BluetoothDevice)
    val connectionState: Channel<ConnectionState>
    fun connectToPeripheral(peripheral: BluetoothDevice, callback: (BluetoothDevice) -> Unit)
}
