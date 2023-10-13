package com.kl3jvi.yonda.manager.scanner

import android.util.Log
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.flow.callbackFlow
import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat
import no.nordicsemi.android.support.v18.scanner.ScanCallback
import no.nordicsemi.android.support.v18.scanner.ScanFilter
import no.nordicsemi.android.support.v18.scanner.ScanResult
import no.nordicsemi.android.support.v18.scanner.ScanSettings

internal fun BluetoothLeScannerCompat.scanFlow(
    settings: ScanSettings = ScanSettings.Builder().build(),
    filters: List<ScanFilter> = emptyList(),
) = callbackFlow {
    val callback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            trySend(result)
                .onFailure {
                    Log.e("FlowScanCallback", "On send scan result", it)
                }
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>) {
            results.forEach { result ->
                trySend(result)
                    .onFailure { sendError ->
                        Log.e("FlowScanCallback", "On send batch scan results", sendError)
                    }
            }
        }

        override fun onScanFailed(errorCode: Int) {
            Log.e("FlowScanCallback", "Scan failed $errorCode")
        }
    }

    startScan(filters, settings, callback)


    Log.i("FlowScanCallback", "Start scan with filter $filters and settings $settings")

    Unit
    awaitClose {
        Log.i("FlowScanCallback", "Stop scan with filter $filters and settings $settings")
        stopScan(callback)
    }
}
