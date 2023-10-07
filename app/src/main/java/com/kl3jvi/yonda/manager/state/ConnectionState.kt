package com.kl3jvi.yonda.manager

import android.bluetooth.BluetoothDevice


// Sealed class representation of connection state
sealed class ConnectionState(val device: BluetoothDevice) {
    class Disconnected(device: BluetoothDevice) : ConnectionState(device)
    class Connecting(device: BluetoothDevice) : ConnectionState(device)
    class Connected(device: BluetoothDevice) : ConnectionState(device)
    class Ready(device: BluetoothDevice) : ConnectionState(device)
    class Disconnecting(device: BluetoothDevice) : ConnectionState(device)
    class Failed(
        device: BluetoothDevice,
        private val status: Int
    ) : ConnectionState(device) {
        val error = when (status) {
            -1 -> "Device disconnected"
            -2 -> "Device not supported"
            -3 -> "Null attribute"
            -4 -> "Request failed"
            -5 -> "Timeout"
            -6 -> "Validation"
            -7 -> "Cancelled"
            -8 -> "Not enabled"
            -100 -> "Bluetooth disabled"
            else -> "Unknown error"
        }
    }
}

fun ConnectionState.getStateAsString(): String {
    return when (this) {
        is ConnectionState.Disconnected -> "Disconnected"
        is ConnectionState.Connecting -> "Connecting"
        is ConnectionState.Connected -> "Connected"
        is ConnectionState.Ready -> "Ready"
        is ConnectionState.Disconnecting -> "Disconnecting"
        is ConnectionState.Failed -> "Failed with error: ${this.error}"
    }
}