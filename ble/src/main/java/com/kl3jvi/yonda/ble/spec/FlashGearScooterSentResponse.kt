package com.kl3jvi.yonda.ble.spec

import android.bluetooth.BluetoothDevice
import android.util.Log
import no.nordicsemi.android.ble.callback.DataSentCallback
import no.nordicsemi.android.ble.callback.FailCallback
import no.nordicsemi.android.ble.data.Data

abstract class FlashGearScooterSentResponse : DataSentCallback {
    override fun onDataSent(device: BluetoothDevice, data: Data) {
        Log.d("FlashGearScooterSentResponse", "onDataSent: $data")
    }
}

