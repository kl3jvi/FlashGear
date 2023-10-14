package com.kl3jvi.yonda.manager

import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
import android.content.Context
import com.kl3jvi.yonda.manager.observers.ConnectionObserverComposite
import com.kl3jvi.yonda.manager.observers.SuspendConnectionObserver
import com.kl3jvi.yonda.manager.providers.ConnectionStateProvider
import kotlinx.coroutines.CoroutineScope
import no.nordicsemi.android.ble.BleManager

abstract class UnsafeBleManager(
    scope: CoroutineScope,
    context: Context,
) : BleManager(context),
    ConnectionStateProvider,
    FlashGearBleManager {

    private val connectionObservers = ConnectionObserverComposite(scope = scope)

    init {
        connectionObserver = connectionObservers
    }

    fun readCharacteristicUnsafe(characteristic: BluetoothGattCharacteristic?) =
        readCharacteristic(characteristic)

    fun writeCharacteristicUnsafe(characteristic: BluetoothGattCharacteristic?, data: ByteArray) =
        writeCharacteristic(characteristic, data, WRITE_TYPE_DEFAULT)

    fun setNotificationCallbackUnsafe(characteristic: BluetoothGattCharacteristic?) =
        setNotificationCallback(characteristic)

    fun enableNotificationsUnsafe(characteristic: BluetoothGattCharacteristic?) =
        enableNotifications(characteristic)


    override fun subscribeOnConnectionState(observer: SuspendConnectionObserver) {
        connectionObservers.addObserver(observer)
        connectionObserver = connectionObservers
    }

    override fun unsubscribeConnectionState(observer: SuspendConnectionObserver) {
        connectionObservers.removeObserver(observer)
        connectionObserver = connectionObservers
    }
}
