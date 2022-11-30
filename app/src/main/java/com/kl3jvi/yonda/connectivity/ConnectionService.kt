package com.kl3jvi.yonda.connectivity

import android.bluetooth.le.ScanResult
import android.content.Context
import com.welie.blessed.BluetoothCentralManager
import com.welie.blessed.BluetoothPeripheral
import com.welie.blessed.ScanFailure
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ConnectionService(
    private val appContext: Context
) : KoinComponent {

    private val central: BluetoothCentralManager by inject()

    /**
     * "Scan for BLE devices and call the onSuccess function when a device is found, or call the
     * onError function if there's an error."
     *
     * The first parameter is a function that takes a `Pair<BluetoothPeripheral, ScanResult>` and
     * returns nothing. The second parameter is a function that takes a `ScanFailure` and returns
     * nothing
     *
     * @param onSuccess This is a callback that will be called when a device is found. It will be
     * called with a Pair of BluetoothPeripheral and ScanResult.
     * @param onError (ScanFailure) -> Unit
     */
    fun scanBleDevices(
        onSuccess: (Pair<BluetoothPeripheral, ScanResult>) -> Unit,
        onError: (ScanFailure) -> Unit
    ) {
        central.scanForPeripherals(
            resultCallback = { bluetoothPeripheral: BluetoothPeripheral, scanResult: ScanResult ->
                onSuccess(bluetoothPeripheral to scanResult)
            },
            scanError = { onError(it) }
        )
    }


    fun stopScanning() = central.stopScan()

}