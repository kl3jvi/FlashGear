package com.kl3jvi.yonda.ble.spec

import android.bluetooth.BluetoothDevice
import com.kl3jvi.nb_api.command.ScooterCommand
import kotlinx.coroutines.flow.StateFlow


interface FlashGear {
    val state: StateFlow<State>

    enum class State {
        LOADING,
        READY,
        NOT_AVAILABLE
    }

    /**
     * Connects to the device.
     */
    suspend fun connectScooter(device: BluetoothDevice)

    /**
     * Disconnects from the device.
     */
    fun release()


    suspend fun sendCommand(command: ScooterCommand)
}