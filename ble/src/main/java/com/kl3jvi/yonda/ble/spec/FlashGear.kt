package com.kl3jvi.yonda.ble.spec

import android.bluetooth.BluetoothDevice
import com.kl3jvi.nb_api.command.ScooterCommand
import kotlinx.coroutines.flow.StateFlow
import no.nordicsemi.android.ble.callback.DataSentCallback
import no.nordicsemi.android.ble.callback.FailCallback
import no.nordicsemi.android.ble.callback.profile.ProfileDataCallback

interface FlashGear : ProfileDataCallback, FailCallback, DataSentCallback {
    val state: StateFlow<State>

    enum class State {
        LOADING,
        READY,
        NOT_AVAILABLE,
    }

    suspend fun connectScooter(device: BluetoothDevice)

    fun release()

    suspend fun sendCommand(command: ScooterCommand)
}