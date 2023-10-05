package com.kl3jvi.yonda.connectivity

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.content.Context
import android.util.Log
import no.nordicsemi.android.ble.BleManager
import java.util.UUID

class FlashGearBluetoothManager(context: Context) : BleManager(context) {

    private var commandCharacteristic: BluetoothGattCharacteristic? = null
    private var readDataCharacteristic: BluetoothGattCharacteristic? = null

    override fun log(priority: Int, message: String) {
        when (priority) {
            Log.ASSERT -> Log.e("FlashGearLogger", message)
            Log.DEBUG -> Log.d("FlashGearLogger", message)
            Log.ERROR -> Log.e("FlashGearLogger", message)
            Log.INFO -> Log.i("FlashGearLogger", message)
            Log.VERBOSE -> Log.v("FlashGearLogger", message)
            Log.WARN -> Log.w("FlashGearLogger", message)
        }
    }


    override fun getGattCallback(): BleManagerGattCallback {
        return FlashGearBleManagerGattCallback()
    }

    fun sendCommandToDevice(command: ByteArray) {
        commandCharacteristic?.let { characteristic ->
            characteristic.value = command
            writeCharacteristic(
                characteristic,
                command,
                1,
                1,
                BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
            ).done {
                Log.e("Done Writing", "Done $it")
            }.fail { device, status ->
                Log.e("Failed Writing", "Failed $device $status")
            }.enqueue()
        } ?: Log.e("Error", "Characteristic is null")
    }


    private inner class FlashGearBleManagerGattCallback : BleManagerGattCallback() {

        // Called when the app should connect to the device's GATT server.
        override fun isRequiredServiceSupported(gatt: BluetoothGatt): Boolean {
            val service = gatt.getService(XIAOMI_SERVICE)
            commandCharacteristic = service?.getCharacteristic(CHAR_WRITE)
            readDataCharacteristic = service?.getCharacteristic(CHAR_READ)
            return commandCharacteristic != null && readDataCharacteristic != null
        }

        override fun initialize() {
            readDataCharacteristic?.let {
                beginAtomicRequestQueue()
                    .add(enableNotifications(it))
                    .enqueue()
            }
        }


        override fun onServicesInvalidated() {
            Log.e("Error", "Services invalidated")
        }

        // Handle characteristic updates
        override fun onCharacteristicNotified(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic
        ) {
            if (characteristic.uuid == CHAR_READ) {
                // handle new data
                val data = characteristic.value
                // do whatever you want with the data
                Log.e("DATA", data.toString())
            }
        }
    }


    companion object {
        val XIAOMI_SERVICE: UUID = UUID.fromString("6e400001-b5a3-f393-e0a9-e50e24dcca9e")
        val CHAR_WRITE: UUID = UUID.fromString("6e400002-b5a3-f393-e0a9-e50e24dcca9e")
        val CHAR_READ: UUID = UUID.fromString("6e400003-b5a3-f393-e0a9-e50e24dcca9e")
    }
}