package com.kl3jvi.yonda.connectivity

import android.bluetooth.le.ScanResult
import android.content.Context
import android.util.Log
import com.welie.blessed.BluetoothCentralManager
import com.welie.blessed.BluetoothPeripheral
import org.koin.core.component.KoinComponent

class ConnectionService(
    private val appContext: Context
) : KoinComponent {

    fun scanBleDevices() {
        BluetoothCentralManager(appContext).scanForPeripherals(
            resultCallback = { bluetoothPeripheral: BluetoothPeripheral, scanResult: ScanResult ->
                Log.e(bluetoothPeripheral.name, "${scanResult.device}")
            },
            scanError = {
                Log.e("Error", "${it.value}")
            }
        )
        BluetoothCentralManager(appContext)
    }
}