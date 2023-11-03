package com.kl3jvi.yonda.manager.scanner

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import com.kl3jvi.yonda.models.DiscoveredBluetoothDevice
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat
import no.nordicsemi.android.support.v18.scanner.ScanCallback
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

        return scanner.scanFlow(provideSettings(), provideFilterForDefaultScan())
            .map(::DiscoveredBluetoothDevice)
    }

    override fun stopScanning() {
        scanner.stopScan(object : ScanCallback() {})
    }

    private fun provideFilterForDefaultScan(): List<ScanFilter> {
        return listOf(
//            ScanFilter.Builder()
//                .setManufacturerData(
//                    16974,
//                    byteArrayOf(40, 2, 0, 0, 0, (-43).toByte()),
//                    byteArrayOf(0xff.toByte(), 0xff.toByte(), 0x00, 0x00, 0x00, 0xff.toByte())
//                )
//                .setManufacturerData(
//                    0x4e42, // Company Identifier Code
//                    byteArrayOf(0x21, 0x00, 0x00, 0x00, 0x00, 0xde.toByte()),
//                    byteArrayOf(0xff.toByte(), 0x00, 0x00, 0x00, 0x00, 0xff.toByte())
//                ).build()
//            0x21 represented in number is 33
//            0x4e42 represented in number is 20034
//            16974 represented in byte is 0x424E
        )
    }

    private fun provideSettings(): ScanSettings {
        return ScanSettings.Builder()
            .setLegacy(false)
            .setScanMode(ScanSettings.SCAN_MODE_BALANCED)
            .setUseHardwareBatchingIfSupported(true)
            .setReportDelay(1000)
            .build()
    }
}
