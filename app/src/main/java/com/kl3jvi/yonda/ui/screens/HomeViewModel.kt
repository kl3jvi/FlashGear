package com.kl3jvi.yonda.ui.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kl3jvi.nb_api.command.ampere.Ampere
import com.kl3jvi.yonda.ble.model.DiscoveredBluetoothDevice
import com.kl3jvi.yonda.ble.repository.FlashGearRepository
import com.kl3jvi.yonda.ble.scanner.FlashGearScanner
import com.kl3jvi.yonda.ble.scanner.ScanningState
import com.kl3jvi.yonda.ext.delayEachFor
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

private const val FILTER_RSSI = -20 // [dBm]

class HomeViewModel(
    private val repository: FlashGearRepository,
    scanner: FlashGearScanner,
) : ViewModel() {
    private val filterConfig =
        MutableStateFlow(
            DevicesScanFilter(
                filterNearbyOnly = false,
                filterWithNames = true,
            ),
        )

    init {
        repository.state.onEach {
            Log.e("HomeViewModel", "connectionState: $it")
        }.launchIn(viewModelScope)
    }

    val state =
        filterConfig.combine(scanner.getScannerState()) { config, result ->
            when (result) {
                is ScanningState.DevicesDiscovered -> result.applyFilters(config)
                else -> result
            }
        }.delayEachFor(1000)

    private fun ScanningState.DevicesDiscovered.applyFilters(config: DevicesScanFilter) =
        ScanningState.DevicesDiscovered(
            devices
                .filter { !config.filterNearbyOnly || it.highestRssi >= FILTER_RSSI }
                .filter { !config.filterWithNames || it.name != null },
        )

    fun connect(it: DiscoveredBluetoothDevice) {
        val exceptionHandler = CoroutineExceptionHandler { _, _ -> }
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            // This method may throw an exception if the connection fails,
            // Bluetooth is disabled, etc.
            // The exception will be caught by the exception handler and will be ignored.
            repository.connectScooter(it.device)
        }
    }

    fun sendCommand()  {
        viewModelScope.launch(Dispatchers.IO) {
            repository.sendCommand(Ampere())
        }
    }
}

data class DevicesScanFilter(
    val filterNearbyOnly: Boolean,
    val filterWithNames: Boolean,
)
