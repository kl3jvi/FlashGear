package com.kl3jvi.yonda.ble.spec

import android.bluetooth.BluetoothDevice
import android.util.Log
import no.nordicsemi.android.ble.callback.DataReceivedCallback
import no.nordicsemi.android.ble.data.Data

abstract class FlashGearScooterResponse : DataReceivedCallback {
    override fun onDataReceived(device: BluetoothDevice, data: Data) {
        Log.d("FlashGearScooterResponse", "onDataReceived: $data")
    }
}
