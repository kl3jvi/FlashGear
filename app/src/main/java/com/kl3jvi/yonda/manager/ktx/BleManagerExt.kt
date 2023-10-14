package com.kl3jvi.yonda.manager.ktx

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothProfile
import com.kl3jvi.yonda.manager.observers.SuspendConnectionObserver
import com.kl3jvi.yonda.manager.providers.ConnectionStateProvider
import com.kl3jvi.yonda.manager.state.ConnectionState
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import no.nordicsemi.android.ble.observer.ConnectionObserver


fun ConnectionStateProvider.stateAsFlow() = callbackFlow {
    val observer = getConnectionObserver()
    send(getConnectionStateFrom(this@stateAsFlow))
    subscribeOnConnectionState(observer)
    awaitClose { unsubscribeConnectionState(observer) }
}

private fun ProducerScope<ConnectionState>.getConnectionObserver() = object : SuspendConnectionObserver {
    override suspend fun onDeviceConnecting(device: BluetoothDevice) {
        send(ConnectionState.Connecting)
    }

    override suspend fun onDeviceConnected(device: BluetoothDevice) {
        send(ConnectionState.Initializing)
    }

    override suspend fun onDeviceFailedToConnect(device: BluetoothDevice, reason: Int) {
        send(ConnectionState.Disconnected(parseDisconnectedReason(reason)))
    }

    override suspend fun onDeviceReady(device: BluetoothDevice) {
        send(ConnectionState.Ready)
    }

    override suspend fun onDeviceDisconnecting(device: BluetoothDevice) {
        send(ConnectionState.Disconnecting)
    }

    override suspend fun onDeviceDisconnected(device: BluetoothDevice, reason: Int) {
        send(ConnectionState.Disconnected(parseDisconnectedReason(reason)))
    }
}

private fun parseDisconnectedReason(
    reason: Int
): ConnectionState.Disconnected.Reason =
    when (reason) {
        ConnectionObserver.REASON_SUCCESS -> ConnectionState.Disconnected.Reason.SUCCESS
        ConnectionObserver.REASON_TERMINATE_LOCAL_HOST ->
            ConnectionState.Disconnected.Reason.TERMINATE_LOCAL_HOST

        ConnectionObserver.REASON_TERMINATE_PEER_USER ->
            ConnectionState.Disconnected.Reason.TERMINATE_PEER_USER

        ConnectionObserver.REASON_LINK_LOSS -> ConnectionState.Disconnected.Reason.LINK_LOSS
        ConnectionObserver.REASON_NOT_SUPPORTED -> ConnectionState.Disconnected.Reason.NOT_SUPPORTED
        ConnectionObserver.REASON_CANCELLED -> ConnectionState.Disconnected.Reason.CANCELLED
        ConnectionObserver.REASON_TIMEOUT -> ConnectionState.Disconnected.Reason.TIMEOUT
        else -> ConnectionState.Disconnected.Reason.UNKNOWN
    }


private fun getConnectionStateFrom(
    connectionStateProvider: ConnectionStateProvider
): ConnectionState {
    return when (connectionStateProvider.getConnectionState()) {
        BluetoothProfile.STATE_CONNECTING -> ConnectionState.Connecting
        BluetoothProfile.STATE_CONNECTED -> if (connectionStateProvider.isReady()) {
            ConnectionState.Ready
        } else {
            ConnectionState.Initializing
        }

        BluetoothProfile.STATE_DISCONNECTING -> ConnectionState.Disconnecting
        else -> ConnectionState.Disconnected(ConnectionState.Disconnected.Reason.UNKNOWN)
    }
}