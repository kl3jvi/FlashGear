package com.kl3jvi.yonda.ble

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.content.Context
import android.util.Log
import com.kl3jvi.nb_api.command.ScooterCommand
import com.kl3jvi.nb_api.command.locking.CheckLock
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
import no.nordicsemi.android.ble.BleManager
import no.nordicsemi.android.ble.ConnectionPriorityRequest
import no.nordicsemi.android.ble.data.Data
import no.nordicsemi.android.ble.ktx.bondingStateAsFlow
import no.nordicsemi.android.ble.ktx.getCharacteristic
import no.nordicsemi.android.ble.ktx.state.BondState
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
    private var notifyCharacteristic: BluetoothGattCharacteristic? = null

    override val bondState =
        bondingStateAsFlow().map {
            when (it) {
                BondState.Bonded -> FlashGear.BondState.BONDED
                BondState.Bonding -> FlashGear.BondState.BONDING
                BondState.NotBonded -> FlashGear.BondState.NOT_BONDED
            }
        }.stateIn(scope, SharingStarted.Lazily, FlashGear.BondState.NOT_BONDED)

    override val state =
        stateAsFlow().map {
            when (it) {
                is ConnectionState.Connecting,
                is ConnectionState.Initializing,
                -> FlashGear.State.LOADING

                is ConnectionState.Ready -> FlashGear.State.READY

                is ConnectionState.Disconnecting,
                is ConnectionState.Disconnected,
                -> FlashGear.State.NOT_AVAILABLE
            }
        }.stateIn(scope, SharingStarted.Lazily, FlashGear.State.NOT_AVAILABLE)

    override fun onDeviceReady() {
        super.onDeviceReady()
        requestConnectionPriority(
            ConnectionPriorityRequest.CONNECTION_PRIORITY_HIGH,
        ).enqueue()
    }

    override suspend fun sendCommand(command: ScooterCommand) {

        writeCharacteristic(
            writeCharacteristic,
            Data(CheckLock().getRequestString().hexToBytes()),
            BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT,
        )
            .with(this)
            .fail(this)
            .suspend()
    }

    override suspend fun connectScooter(device: BluetoothDevice) {
        connect(device)
            .retry(3, 300)
            .useAutoConnect(false)
            .timeout(3000)
            .suspend()
    }

    override fun release() {
        scope.cancel()
        val wasConnected = isReady
        cancelQueue()
        if (wasConnected) {
            disconnect().enqueue()
        }
    }

    override fun log(
        priority: Int,
        message: String,
    ) {
        Log.println(priority, "FLASH_GEAR_MANAGER", message)
    }

    override fun getMinLogPriority(): Int {
        return Log.VERBOSE
    }

    override fun isRequiredServiceSupported(gatt: BluetoothGatt): Boolean {
        gatt.getService(SERVICE)?.apply {
            writeCharacteristic =
                getCharacteristic(
                    WRITE_CHARACTERISTIC,
                    BluetoothGattCharacteristic.PROPERTY_WRITE,
                )
            notifyCharacteristic =
                getCharacteristic(
                    FlashGearSpec.NOTIFY_CHARACTERISTIC,
                    BluetoothGattCharacteristic.PROPERTY_NOTIFY,
                )
            return writeCharacteristic != null && notifyCharacteristic != null
        }

        return false
    }

    override fun initialize() {
        if (writeCharacteristic == null || notifyCharacteristic == null) {
            return
        }

        requestMtu(34)
            .with { device, mtu ->
                Log.d("FLASH_GEAR_MANAGER", "MTU set to $mtu on ${device.address}")
            }.enqueue()

        setNotificationCallback(notifyCharacteristic)
            .with(this)

        enableNotifications(notifyCharacteristic)
            .with(this)
            .fail(this)
            .enqueue()
    }

    override fun onServicesInvalidated() {
        writeCharacteristic = null
        notifyCharacteristic = null
    }

    override fun onDataReceived(
        device: BluetoothDevice,
        data: Data,
    ) {
        log(Log.INFO, "Data received: ${data.value}")
    }

    override fun onRequestFailed(
        device: BluetoothDevice,
        status: Int,
    ) {
        log(Log.WARN, "Request failed: $status")
    }

    override fun onDataSent(
        device: BluetoothDevice,
        data: Data,
    ) {
        log(Log.INFO, "Data sent: ${data.value}")
    }
}
