package com.kl3jvi.yonda.ble.scanner

import android.content.Context
import kotlinx.coroutines.flow.Flow

interface FlashGearScanner {
    fun getScannerState(): Flow<ScanningState>
}
