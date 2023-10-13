package com.kl3jvi.yonda.manager

import android.bluetooth.BluetoothDevice

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
