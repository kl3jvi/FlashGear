package com.kl3jvi.yonda.ble

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.content.Context
import android.util.Log
import com.kl3jvi.nb_api.command.ScooterCommand
import com.kl3jvi.nb_api.command.ampere.Ampere
import com.kl3jvi.nb_api.command.battery.Battery
import com.kl3jvi.nb_api.command.cruise.CheckCruise
import com.kl3jvi.nb_api.command.util.hexToBytes
import com.kl3jvi.yonda.ble.spec.FlashGear
import com.kl3jvi.yonda.ble.spec.FlashGearSpec
import com.kl3jvi.yonda.ble.spec.FlashGearSpec.Companion.SERVICE
import com.kl3jvi.yonda.ble.spec.FlashGearSpec.Companion.WRITE_CHARACTERISTIC
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import no.nordicsemi.android.ble.BleManager
import no.nordicsemi.android.ble.ConnectionPriorityRequest
import no.nordicsemi.android.ble.ktx.getCharacteristic
import no.nordicsemi.android.ble.ktx.state.ConnectionState
import no.nordicsemi.android.ble.ktx.stateAsFlow
import no.nordicsemi.android.ble.ktx.suspend


class FlashGearManager(
    context: Context,
) : FlashGear by FlashGearManagerImpl(context)

private class FlashGearManagerImpl(
    context: Context,
) : BleManager(context), FlashGear {


    private val scope = CoroutineScope(Dispatchers.IO)

    private var writeCharacteristic: BluetoothGattCharacteristic? = null
    private var readCharacteristic: BluetoothGattCharacteristic? = null

    override fun onDeviceReady() {
        super.onDeviceReady()
        requestConnectionPriority(
            ConnectionPriorityRequest.CONNECTION_PRIORITY_HIGH,
        ).enqueue()
    }

    override val state = stateAsFlow()
        .map {
            when (it) {
                is ConnectionState.Connecting,
                is ConnectionState.Initializing -> FlashGear.State.LOADING

                is ConnectionState.Ready -> FlashGear.State.READY
                is ConnectionState.Disconnecting,
                is ConnectionState.Disconnected -> FlashGear.State.NOT_AVAILABLE
            }
        }
        .stateIn(scope, SharingStarted.Lazily, FlashGear.State.NOT_AVAILABLE)

    override suspend fun sendCommand(command: ScooterCommand) {
        val commands = listOf(
            Ampere(),
            Battery(),
            CheckCruise()
        ).map { it.getRequestString().hexToBytes() }
            .random()
        writeCharacteristic(
            writeCharacteristic,
            commands,
            BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
        ).done { device ->
            Log.i("FLASH_GEAR_MANAGER", "Sent to ${device.address}: $commands")
        }.fail { device, status ->
            Log.i("FLASH_GEAR_MANAGER", "Failed to send to ${device.address}: $status")
        }.suspend()
    }


    override suspend fun connectScooter(device: BluetoothDevice) = connect(device)
        .retry(3, 600)
        .useAutoConnect(false)
        .timeout(3000)
        .suspend()

    override fun release() {
        scope.cancel()
        val wasConnected = isReady
        cancelQueue()
        if (wasConnected) {
            disconnect().enqueue()
        }
    }

    override fun log(priority: Int, message: String) {
        Log.println(priority, "FLASH_GEAR_MANAGER", message)
    }

    override fun getMinLogPriority(): Int {
        return Log.VERBOSE
    }

    override fun isRequiredServiceSupported(gatt: BluetoothGatt): Boolean {
        gatt.getService(SERVICE)?.apply {
            writeCharacteristic = getCharacteristic(
                WRITE_CHARACTERISTIC,
                BluetoothGattCharacteristic.PROPERTY_WRITE
            )
            readCharacteristic = getCharacteristic(
                FlashGearSpec.READ_CHARACTERISTIC,
                BluetoothGattCharacteristic.PROPERTY_NOTIFY
            )
            return writeCharacteristic != null && readCharacteristic != null
        }
        return false
    }


    override fun initialize() {

        if (writeCharacteristic == null || readCharacteristic == null) {
            return
        }


        setNotificationCallback(readCharacteristic)
            .with { device, data ->
                println("Received from ${device.address}: ${data.value?.toString(Charsets.UTF_8)}")
                Log.i(
                    "FLASH_GEAR_MANAGER",
                    "Received from ${device.address}: ${data.value?.toString(Charsets.UTF_8)}"
                )
            }
//
//        waitForNotification(readCharacteristic)
//            .with { device, data ->
//                Log.i(
//                    "FLASH_GEAR_MANAGER",
//                    "Received from ${device.address}: ${data.value?.toString(Charsets.UTF_8)}"
//                )
//            }
//            .enqueue()

        enableNotifications(readCharacteristic)
            .done { device ->
                Log.i("FLASH_GEAR_MANAGER", "Enabled notifications for ${device.address}")
            }
            .fail { device, status ->
                Log.i(
                    "FLASH_GEAR_MANAGER",
                    "Failed to enable notifications for ${device.address}: $status"
                )
            }.enqueue()
    }


    override fun onServicesInvalidated() {
        writeCharacteristic = null
        readCharacteristic = null
    }

}

