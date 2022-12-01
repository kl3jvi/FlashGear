package com.kl3jvi.yonda.connectivity

import android.bluetooth.le.ScanResult
import android.util.Log
import com.welie.blessed.BluetoothCentralManager
import com.welie.blessed.BluetoothPeripheral
import com.welie.blessed.ScanFailure
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ConnectionServiceImpl : ConnectionService, KoinComponent {

    private val central: BluetoothCentralManager by inject()

    override fun scanBleDevices(
        onSuccess: (BluetoothPeripheral) -> Unit,
        onError: (ScanFailure?, Throwable?) -> Unit
    ) {
        runCatching {
            central.scanForPeripherals(
                resultCallback = { bluetoothPeripheral: BluetoothPeripheral, _: ScanResult ->
                    onSuccess(bluetoothPeripheral)
                },
                scanError = {
                    Log.e("Error", "happened ${it.name}")
                    onError(it, null)
                }
            )
        }.onFailure {
            onError(null, it)
        }
    }

    override fun isBluetoothEnabled() = central.isBluetoothEnabled


    override fun stopScanning() = central.stopScan()

    override suspend fun connectPeripheral(bluetoothPeripheral: BluetoothPeripheral) =
        withContext(Dispatchers.IO) {
            runCatching { central.connectPeripheral(bluetoothPeripheral) }
        }
}