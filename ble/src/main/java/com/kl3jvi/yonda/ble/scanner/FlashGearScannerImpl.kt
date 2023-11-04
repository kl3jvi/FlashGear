package com.kl3jvi.yonda.ble.scanner

import com.kl3jvi.yonda.ble.model.DiscoveredBluetoothDevice
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat
import no.nordicsemi.android.support.v18.scanner.ScanCallback
import no.nordicsemi.android.support.v18.scanner.ScanResult
import no.nordicsemi.android.support.v18.scanner.ScanSettings

class FlashGearScannerImpl : FlashGearScanner {
    override fun getScannerState(): Flow<ScanningState> =
        callbackFlow {
            // concurrency supporting list that will add new devices to the list
            // and remove old ones if they are not discovered in 10 seconds
            val scanResults = mutableListOf<DiscoveredBluetoothDevice>()

            val scanCallback: ScanCallback =
                object : ScanCallback() {
                    override fun onScanResult(
                        callbackType: Int,
                        result: ScanResult,
                    ) {
                        super.onScanResult(callbackType, result)
                        scanResults.accumulateUniqueDevices(DiscoveredBluetoothDevice(result))
                        trySend(ScanningState.DevicesDiscovered(scanResults))
                    }

                    override fun onBatchScanResults(results: MutableList<ScanResult>) {
                        super.onBatchScanResults(results)
                        if (results.isNotEmpty()) {
                            results.map(::DiscoveredBluetoothDevice)
                                .forEach { device ->
                                    scanResults.accumulateUniqueDevices(device)
                                }
                            trySend(ScanningState.DevicesDiscovered(scanResults))
                        }
                    }

                    override fun onScanFailed(errorCode: Int) {
                        super.onScanFailed(errorCode)
                        trySend(ScanningState.Error(errorCode))
                    }
                }
            trySend(ScanningState.Loading)

            val settings =
                ScanSettings.Builder()
                    .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                    .setLegacy(false)
                    .setReportDelay(500)
                    .setUseHardwareBatchingIfSupported(false)
                    .build()
            val scanner = BluetoothLeScannerCompat.getScanner()

            scanner.startScan(null, settings, scanCallback)

            awaitClose {
                scanner.stopScan(scanCallback)
            }
        }
}

fun MutableList<DiscoveredBluetoothDevice>.accumulateUniqueDevices(newDevice: DiscoveredBluetoothDevice) {
    val existingDevice = find { it.address == newDevice.address }
    if (existingDevice != null) {
        // Update the existing device with new scan result
        existingDevice.update(newDevice.scanResult!!)
    } else {
        // Add new device to the list
        add(newDevice)
    }
}
