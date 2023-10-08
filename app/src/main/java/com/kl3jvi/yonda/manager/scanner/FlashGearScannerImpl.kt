package com.kl3jvi.yonda.manager.scanner

import android.Manifest
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.ParcelUuid
import android.util.Log
import androidx.core.app.ActivityCompat
import com.kl3jvi.yonda.ext.withLock
import com.kl3jvi.yonda.models.DiscoveredBluetoothDevice
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.sync.Mutex
import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat
import no.nordicsemi.android.support.v18.scanner.ScanFilter
import no.nordicsemi.android.support.v18.scanner.ScanSettings
import java.util.UUID

class FlashGearScannerImpl(
    private val bluetoothManager: BluetoothManager?,
    private val scanner: BluetoothLeScannerCompat,
    private val context: Context,

) : FlashGearScanner {
    override fun findScooterDevices(): Flow<Iterable<DiscoveredBluetoothDevice>> {
        val devices = ArrayList(getAlreadyConnectedDevices())
        val mutex = Mutex()

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT,
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            throw SecurityException(
                "You should request BLUETOOTH_CONNECT before on Android API > 31",
            )
        }

        return merge(
            devices.asFlow(),
            scanner.scanFlow(provideSettings(), provideFilterForDefaultScan()).map {
                DiscoveredBluetoothDevice(it)
            },
        ).map { discoveredBluetoothDevice ->
            var mutableDevicesList: List<DiscoveredBluetoothDevice> = emptyList()
            withLock(mutex, "scan_map") {
                val alreadyExistDBD = devices.getOrNull(
                    devices.indexOf(discoveredBluetoothDevice),
                )
                if (alreadyExistDBD != null) {
                    val scanResult = discoveredBluetoothDevice.scanResult
                    if (scanResult != null) {
                        alreadyExistDBD.update(scanResult)
                    }
                } else {
                    Log.i("FlashGearScannerImpl", "Find new device $discoveredBluetoothDevice")
                    devices.add(discoveredBluetoothDevice)
                }
                mutableDevicesList = devices.toList()
            }
            return@map mutableDevicesList
        }
    }

    private fun provideFilterForDefaultScan(): List<ScanFilter> {
        return listOf<ScanFilter>(
            ScanFilter.Builder()
                .setServiceUuid(ParcelUuid.fromString(XIAOMI_SERVICE.toString()))
                .build(),
        )
    }

    private fun provideSettings(): ScanSettings {
        return ScanSettings.Builder()
            .setLegacy(false)
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .setUseHardwareBatchingIfSupported(true)
            .setReportDelay(5000)
            .build()
    }

    private fun getAlreadyConnectedDevices(): List<DiscoveredBluetoothDevice> {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S &&
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT,
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return emptyList()
        }
        if (bluetoothManager == null) {
            return emptyList()
        }

        return bluetoothManager.getConnectedDevices(BluetoothProfile.GATT).filter {
            it.name?.startsWith(DEVICENAME_PREFIX) == true
        }.map {
            DiscoveredBluetoothDevice(
                device = it,
                nameInternal = it.name,
                rssiInternal = 0,
                previousRssi = 0,
            )
        }
    }

    companion object {
        val XIAOMI_SERVICE: UUID = UUID.fromString("6e400001-b5a3-f393-e0a9-e50e24dcca9e")
        private const val MAC_PREFIX = "00:1B:DC"
        private const val DEVICENAME_PREFIX = "MIScooter"
    }
}
