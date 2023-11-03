package com.kl3jvi.yonda.manager.service

import android.bluetooth.BluetoothDevice
import com.kl3jvi.nb_api.command.ScooterCommand

interface FlashGearService {
    suspend fun connect(device: BluetoothDevice)

    suspend fun disconnect()

    suspend fun sendCommand(command: ScooterCommand)
}
