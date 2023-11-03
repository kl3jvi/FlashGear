package com.kl3jvi.yonda.manager

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.util.Log
import com.kl3jvi.yonda.ext.withLock
import com.kl3jvi.yonda.manager.ktx.stateAsFlow
import com.kl3jvi.yonda.manager.service.BluetoothGattServiceWrapper
import com.kl3jvi.yonda.manager.state.ConnectionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
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
            val connectRequestLocal =
                connect(device)
                    .retry(
                        RECONNECT_COUNT,
                        RECONNECT_TIME_MS.toInt(),
                    )
                    .done { Log.i("Ble Manager", "Connected") }
                    .fail { device, status ->
                        Log.i(
                            "Ble Manager",
                            "Failed to connect ${device.address} with reason $status",
                        )
                    }
                    .useAutoConnect(false)
                    .timeout(10000)
            connectRequestLocal.enqueue()
            connectRequest = connectRequestLocal
            return@withLock
        }
        stateAsFlow().filter { it is ConnectionState.Initializing }.first()
    }

    override suspend fun disconnectDevice() {
        withLock(bleMutex, "disconnect") {
            connectRequest?.cancelPendingConnection()
            disconnect().enqueue()
            return@withLock
        }

        stateAsFlow().filter { it is ConnectionState.Disconnected }.first()
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

    override fun log(
        priority: Int,
        message: String,
    ) {
        Log.println(priority, "FlashGearLogger", message)
    }

    companion object {
        const val RECONNECT_COUNT = 3
        const val RECONNECT_TIME_MS = 1000L
    }
}
