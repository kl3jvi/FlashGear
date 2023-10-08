package com.kl3jvi.yonda.manager

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
import android.content.Context
import com.kl3jvi.yonda.manager.observers.BondingObserverComposite
import com.kl3jvi.yonda.manager.observers.ConnectionObserverComposite
import com.kl3jvi.yonda.manager.observers.SuspendConnectionObserver
import com.kl3jvi.yonda.manager.providers.BondStateProvider
import com.kl3jvi.yonda.manager.providers.ConnectionStateProvider
import kotlinx.coroutines.CoroutineScope
import no.nordicsemi.android.ble.BleManager
import no.nordicsemi.android.ble.observer.BondingObserver

abstract class UnsafeBleManager(
    scope: CoroutineScope,
    context: Context,
) : BleManager(context),
    ConnectionStateProvider,
    BondStateProvider,
    FlashGearBleManager {

    private val connectionObservers = ConnectionObserverComposite(scope = scope)
    private val bondingObservers = BondingObserverComposite()

    init {
        connectionObserver = connectionObservers
        bondingObserver = bondingObservers
    }

    fun readCharacteristicUnsafe(characteristic: BluetoothGattCharacteristic?) =
        readCharacteristic(characteristic)

    fun writeCharacteristicUnsafe(characteristic: BluetoothGattCharacteristic?, data: ByteArray) =
        writeCharacteristic(characteristic, data, WRITE_TYPE_DEFAULT)

    fun setNotificationCallbackUnsafe(characteristic: BluetoothGattCharacteristic?) =
        setNotificationCallback(characteristic)

    fun enableNotificationsUnsafe(characteristic: BluetoothGattCharacteristic?) =
        enableNotifications(characteristic)

    fun enableIndicationsUnsafe(characteristic: BluetoothGattCharacteristic?) =
        enableIndications(characteristic)

    override fun subscribeOnConnectionState(observer: SuspendConnectionObserver) {
        connectionObservers.addObserver(observer)
        connectionObserver = connectionObservers
    }

    override fun unsubscribeConnectionState(observer: SuspendConnectionObserver) {
        connectionObservers.removeObserver(observer)
        connectionObserver = connectionObservers
    }

    @SuppressLint("MissingPermission")
    override fun getBondState(): Int? {
        return bluetoothDevice?.bondState
    }

    override fun subscribeOnBondingState(observer: BondingObserver) {
        bondingObservers.addObserver(observer)
        bondingObserver = bondingObservers
    }

    override fun unsubscribeBondingState(observer: BondingObserver) {
        bondingObservers.removeObserver(observer)
        bondingObserver = bondingObservers
    }
}
