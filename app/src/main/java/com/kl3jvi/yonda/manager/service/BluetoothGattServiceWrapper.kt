package com.kl3jvi.yonda.manager.service

import android.bluetooth.BluetoothGatt
import com.kl3jvi.yonda.manager.UnsafeBleManager

/**
 * Delegate interface for wrap gatt services
 */
interface BluetoothGattServiceWrapper {
    /**
     * Call when device notify about supported service
     * @return true if requested service is present
     */
    fun onServiceReceived(gatt: BluetoothGatt): Boolean

    /**
     * Call when device is ready for reading characteristics
     * Call after {@link #onServiceReceived}
     */
    suspend fun initialize(bleManager: UnsafeBleManager)

    /**
     * Reset stateflows and others stateful component
     * Calls after reconnect to new device or invalidate services
     */
    suspend fun reset(bleManager: UnsafeBleManager)
}
