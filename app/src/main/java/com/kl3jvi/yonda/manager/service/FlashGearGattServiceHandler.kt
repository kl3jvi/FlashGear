package com.kl3jvi.yonda.manager.service

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.util.Log
import com.kl3jvi.nb_api.command.ScooterCommand
import com.kl3jvi.yonda.manager.UnsafeBleManager
import java.util.UUID

class FlashGearGattServiceHandler : BluetoothGattServiceWrapper {
    private var commandCharacteristic: BluetoothGattCharacteristic? = null
    private var readDataCharacteristic: BluetoothGattCharacteristic? = null

    override fun onServiceReceived(gatt: BluetoothGatt): Boolean {
        val service = gatt.getService(XIAOMI_SERVICE)
        commandCharacteristic = service.getCharacteristic(CHAR_WRITE)
        readDataCharacteristic = service.getCharacteristic(CHAR_READ)
        return commandCharacteristic != null && readDataCharacteristic != null
    }

    override suspend fun initialize(bleManager: UnsafeBleManager) {
        bleManager.setNotificationCallbackUnsafe(readDataCharacteristic)
            .with { device, data ->
                println("Received from ${device.address}: ${data.value?.toString(Charsets.UTF_8)}")
            }

        bleManager.enableNotificationsUnsafe(readDataCharacteristic).enqueue()

        readInformation(bleManager)
    }

    override suspend fun sendCommandToDevice(
        command: ScooterCommand,
        bleManager: UnsafeBleManager,
    ) {
        commandCharacteristic?.let { characteristic ->
            val commandInBytes = command.getRequestString().toByteArray(Charsets.UTF_8)
            bleManager.writeCharacteristicUnsafe(characteristic, commandInBytes)
                .done { Log.i("BLE", "Done Writing: $command") }
                .fail { device, status -> Log.e("BLE", "Failed Writing: $device $status") }
                .enqueue()
        } ?: Log.e("BLE", "Command Characteristic is null!")
    }

    private fun readInformation(bleManager: UnsafeBleManager) {
        bleManager.readCharacteristicUnsafe(
            readDataCharacteristic,
        ).with { _, data ->
            val content = data.value ?: return@with
            println("Received: ${String(content)}")
        }.enqueue()
    }

    override suspend fun reset(bleManager: UnsafeBleManager) = Unit

    companion object {
        val XIAOMI_SERVICE: UUID = UUID.fromString("6e400001-b5a3-f393-e0a9-e50e24dcca9e")
        val CHAR_WRITE: UUID = UUID.fromString("6e400002-b5a3-f393-e0a9-e50e24dcca9e")
        val CHAR_READ: UUID = UUID.fromString("6e400003-b5a3-f393-e0a9-e50e24dcca9e")
    }
}
