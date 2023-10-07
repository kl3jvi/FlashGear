package com.kl3jvi.yonda.connectivity

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.content.Context
import android.util.Log
import com.welie.blessed.supportsNotifying
import no.nordicsemi.android.ble.BleManager
import no.nordicsemi.android.ble.data.Data
import java.util.UUID

class FlashGearBluetoothManager(context: Context) : BleManager(context) {

    private var commandCharacteristic: BluetoothGattCharacteristic? = null
    private var readDataCharacteristic: BluetoothGattCharacteristic? = null

    override fun getGattCallback(): BleManagerGattCallback {
        return FlashGearBleManagerGattCallback()
    }

    override fun log(priority: Int, message: String) {
        Log.println(priority, "FlashGearLogger", message)
    }

    fun sendCommandToDevice(command: ByteArray) {
        commandCharacteristic?.let { characteristic ->
            writeCharacteristic(characteristic, Data(command))
                .done { Log.i("BLE", "Done Writing: ${command.toString(Charsets.UTF_8)}") }
                .fail { device, status -> Log.e("BLE", "Failed Writing: $device $status") }
                .enqueue()
        } ?: Log.e("BLE", "Command Characteristic is null!")
    }

    private inner class FlashGearBleManagerGattCallback : BleManagerGattCallback() {

        override fun initialize() {
            readDataCharacteristic?.let { characteristic ->
                setNotificationCallback(characteristic).with { device, data ->
                    Log.d("NOTIFICATION", "Received from ${device.address}: ${data.value?.toString(Charsets.UTF_8)}")
                }
                enableNotifications(characteristic).enqueue()
            } ?: run {
                Log.e("BLE", "Read characteristic is null during initialization!")
            }
        }

        override fun isRequiredServiceSupported(gatt: BluetoothGatt): Boolean {
            val service = gatt.getService(XIAOMI_SERVICE)
            commandCharacteristic = service?.getCharacteristic(CHAR_WRITE)
            readDataCharacteristic = service?.getCharacteristic(CHAR_READ)
            gatt.setCharacteristicNotification(readDataCharacteristic, true)

            return commandCharacteristic != null && readDataCharacteristic != null
        }

        override fun onServicesInvalidated() {
            readDataCharacteristic = null
            commandCharacteristic = null
        }

    }

    companion object {
        val XIAOMI_SERVICE: UUID = UUID.fromString("6e400001-b5a3-f393-e0a9-e50e24dcca9e")
        val CHAR_WRITE: UUID = UUID.fromString("6e400002-b5a3-f393-e0a9-e50e24dcca9e")
        val CHAR_READ: UUID = UUID.fromString("6e400003-b5a3-f393-e0a9-e50e24dcca9e")
    }
}