package com.kl3jvi.yonda.manager

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.util.Log
import com.kl3jvi.yonda.ext.withLock
import com.kl3jvi.yonda.manager.service.BluetoothGattServiceWrapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import no.nordicsemi.android.ble.ConnectRequest
import no.nordicsemi.android.ble.ConnectionPriorityRequest

class FlashGearBluetoothManager(
    context: Context,
    private val scope: CoroutineScope,
    private val flashGearGattServiceHandler: BluetoothGattServiceWrapper,
) : UnsafeBleManager(scope, context) {

    private var connectRequest: ConnectRequest? = null
    private val bleMutex = Mutex()

    override suspend fun connectToDevice(device: BluetoothDevice) {
        withLock(bleMutex, "connect") {
            val connectRequestLocal = connect(device)
                .retry(
                    RECONNECT_COUNT,
                    RECONNECT_TIME_MS.toInt(),
                ).useAutoConnect(true)

            connectRequestLocal.enqueue()

            connectRequest = connectRequestLocal

            return@withLock
        }
    }

    override suspend fun disconnectDevice() {
        withLock(bleMutex, "disconnect") {
            connectRequest?.cancelPendingConnection()
            disconnect().enqueue()
        }
    }

    override fun initialize() {
        if (!isBonded) {
            Log.i("Ble Manager", "Start bond secure")
            ensureBond().enqueue()
        }
    }

    override fun onDeviceReady() {
        super.onDeviceReady()
        requestConnectionPriority(
            ConnectionPriorityRequest.CONNECTION_PRIORITY_HIGH,
        ).enqueue()
        scope.launch(Dispatchers.Default) {
            flashGearGattServiceHandler
                .initialize(
                    this@FlashGearBluetoothManager,
                )
        }
    }

    override fun onServicesInvalidated() {
        scope.launch(Dispatchers.Default) {
            flashGearGattServiceHandler
                .reset(this@FlashGearBluetoothManager)
        }
    }

    companion object {
        const val RECONNECT_COUNT = 1
        const val RECONNECT_TIME_MS = 100L
    }
}
