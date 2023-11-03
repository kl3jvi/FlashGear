package com.kl3jvi.yonda.manager.service

import android.bluetooth.BluetoothDevice
import com.kl3jvi.nb_api.command.ScooterCommand
import com.kl3jvi.yonda.manager.FlashGearBluetoothManager

class FlashGearServiceApiImpl(
    private val bluetoothManager: FlashGearBluetoothManager,
    private val gattServiceHandler: BluetoothGattServiceWrapper,
) : FlashGearService {
    override suspend fun connect(device: BluetoothDevice) {
        bluetoothManager.connectToDevice(device)
    }

    override suspend fun disconnect() {
        bluetoothManager.disconnectDevice()
    }

    override suspend fun sendCommand(command: ScooterCommand) {
        gattServiceHandler.sendCommandToDevice(command, bluetoothManager)
    }
}
