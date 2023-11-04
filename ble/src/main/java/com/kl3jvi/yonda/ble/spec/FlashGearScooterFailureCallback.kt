package com.kl3jvi.yonda.ble.spec

import android.bluetooth.BluetoothDevice
import android.util.Log
import no.nordicsemi.android.ble.callback.FailCallback

abstract class FlashGearScooterFailureCallback : FailCallback {
    override fun onRequestFailed(device: BluetoothDevice, status: Int) {
        Log.d("FlashGearScooterFailureCallback", "onFail: $status")
    }
}