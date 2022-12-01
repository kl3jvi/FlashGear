package com.kl3jvi.yonda.connectivity

import com.welie.blessed.BluetoothPeripheral
import com.welie.blessed.ScanFailure

interface ConnectionService {

    /**
     * "Scan for BLE devices and call the onSuccess function when a device is found, or call the
     * onError function if there's an error."
     *
     *
     * @param onSuccess This is a callback that will be called when a device is found. It will be
     * called with a Pair of BluetoothPeripheral and ScanResult.
     * @param onError (ScanFailure) -> Unit
     */
    fun scanBleDevices(
        onSuccess: (BluetoothPeripheral) -> Unit,
        onError: (ScanFailure?, Throwable?) -> Unit
    )

    /**
     * Returns true if Bluetooth is enabled, false otherwise.
     */
    fun isBluetoothEnabled(): Boolean

    /**
     * Stop scanning for peripherals
     */
    fun stopScanning()

    /**
     * This function connects to a Bluetooth peripheral
     *
     * @param bluetoothPeripheral The BluetoothPeripheral object that you want to connect to.
     */
    suspend fun connectPeripheral(bluetoothPeripheral: BluetoothPeripheral): Result<Unit>
}
