package com.kl3jvi.yonda.manager.scanner

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.ParcelUuid
import androidx.core.app.ActivityCompat
import com.kl3jvi.yonda.manager.service.FlashGearGattServiceHandler.Companion.XIAOMI_SERVICE
import com.kl3jvi.yonda.models.DiscoveredBluetoothDevice
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat
import no.nordicsemi.android.support.v18.scanner.ScanFilter
import no.nordicsemi.android.support.v18.scanner.ScanSettings

class FlashGearScannerImpl(
    private val scanner: BluetoothLeScannerCompat,
    private val context: Context,
) : FlashGearScanner {

    override fun findScooterDevices(): Flow<DiscoveredBluetoothDevice> {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT,
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            throw SecurityException(
                "You should request BLUETOOTH_CONNECT before on Android API > 31",
            )
        }

        return scanner.scanFlow(provideSettings())
            .map { DiscoveredBluetoothDevice(it) }

    }

    private fun provideFilterForDefaultScan(): List<ScanFilter> {
        return listOf<ScanFilter>(
//            ScanFilter.Builder()
//                .setServiceUuid(ParcelUuid.fromString(XIAOMI_SERVICE.toString()))
//                .build(),
        )
    }

    private fun provideSettings(): ScanSettings {
        return ScanSettings.Builder()
            .setLegacy(false)
            .setScanMode(ScanSettings.SCAN_MODE_BALANCED)
            .setUseHardwareBatchingIfSupported(true)
            .setReportDelay(5000)
            .build()
    }
}
