package com.kl3jvi.yonda.ui.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kl3jvi.yonda.ext.accumulateUniqueDevices
import com.kl3jvi.yonda.ext.delayEachFor
import com.kl3jvi.yonda.manager.scanner.FlashGearScanner
import com.kl3jvi.yonda.manager.service.FlashGearService
import com.kl3jvi.yonda.models.DiscoveredBluetoothDevice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicBoolean

private const val TIMEOUT_MS = 2 * 60 * 1000L

class HomeViewModel(
    private val scanner: FlashGearScanner,
    private val flashGearServiceApi: FlashGearService,
) : ViewModel() {

    val state = MutableStateFlow<ScanState>(ScanState.Stopped())
    private val scanStarted = AtomicBoolean(false)
    private var scanJob: Job? = null

    @Synchronized
    fun startScan() {
        if (!scanStarted.compareAndSet(false, true)) {
            Log.i("Scan started", "already")
            return
        }

        scanJob = viewModelScope.launch {
            launch { startBLEDiscover() }
            delay(TIMEOUT_MS)
            if (state.value is ScanState.Searching) {
                state.emit(ScanState.Timeout)
                stopScanning()
            }
        }
    }

    private suspend fun startBLEDiscover() = withContext(Dispatchers.IO) {
        scanner.findScooterDevices()
            .accumulateUniqueDevices()
            .delayEachFor(1000)
            .map<List<DiscoveredBluetoothDevice>, ScanState> {
                ScanState.Founded(it)
            }
            .onStart { emit(ScanState.Searching) }
            .catch { throwable ->
                Log.e("Scan error", throwable.message ?: "Unknown error")
                state.update { ScanState.Stopped() }
            }
            .collect { devices ->
                if (devices is ScanState.Founded)
                    Log.i("Scan result", devices.devices.map { it.name to it.address }.toString())
                state.update { devices }
            }
    }

    fun connect(device: DiscoveredBluetoothDevice) = viewModelScope.launch(Dispatchers.IO) {
        flashGearServiceApi.connect(device.device)
        stopScanning()
    }

    @Synchronized
    fun stopScanning() {
        if (!scanStarted.compareAndSet(true, false)) {
            return
        }
        scanJob?.cancel()
        scanJob = null
        state.update { if (it !is ScanState.Stopped) ScanState.Stopped() else it }
    }

    fun toggleScan(scanning: Boolean) {
        if (scanning) {
            Log.e("Scan", "stop")
            stopScanning()
        } else {
            Log.e("Scan", "start")
            startScan()
        }
    }
}

sealed class ScanState {
    open class Stopped : ScanState()
    data object Searching : ScanState()
    class Founded(val devices: List<DiscoveredBluetoothDevice>) : ScanState()
    object Timeout : Stopped()
}
