package com.kl3jvi.yonda.manager.scanner

import com.kl3jvi.yonda.models.DiscoveredBluetoothDevice
import kotlinx.coroutines.flow.Flow

interface FlashGearScanner {
    fun findScooterDevices(): Flow<DiscoveredBluetoothDevice>

    fun stopScanning()
}
