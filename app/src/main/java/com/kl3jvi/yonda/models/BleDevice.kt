package com.kl3jvi.yonda.models

import com.welie.blessed.BluetoothPeripheral
import com.welie.blessed.ConnectionState

data class BleDevice(
    val peripheral: BluetoothPeripheral,
    var connectionState: ConnectionState = ConnectionState.DISCONNECTED
)
