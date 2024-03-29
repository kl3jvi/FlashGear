package com.kl3jvi.yonda.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import com.kl3jvi.nb_api.command.ampere.Ampere
import com.kl3jvi.yonda.ble.model.DiscoveredBluetoothDevice
import com.kl3jvi.yonda.ble.repository.FlashGearRepository
import com.kl3jvi.yonda.ble.scanner.FlashGearScanner
import com.kl3jvi.yonda.ble.scanner.ScanningState
import com.kl3jvi.yonda.ble.spec.FlashGear
import com.kl3jvi.yonda.ext.launchOnIo
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update

private const val FILTER_RSSI = -50 // [dBm]

class HomeViewModel(
    private val repository: FlashGearRepository,
    scanner: FlashGearScanner,
) : ViewModel() {
    val filterConfig =
        MutableStateFlow(
            DevicesScanFilter(
                filterNearbyOnly = false,
                filterWithNames = false,
            ),
        )


    private val exceptionHandler =
        CoroutineExceptionHandler { context, throwable ->
            Log.e("HomeViewModel", "Error occurred $context: ", throwable)
        }

    val state =
        combine(
            filterConfig,
            scanner.getScannerState(),
            repository.state
        ) { config, result, state ->
            when (result) {
                is ScanningState.DevicesDiscovered -> result.applyFiltersAndSort(config, state)

                else -> result
            }
        }

    private fun ScanningState.DevicesDiscovered.applyFiltersAndSort(
        config: DevicesScanFilter,
        state: Pair<FlashGear.State, String?>
    ): ScanningState.DevicesDiscovered = apply {
        val (currentState, currentMacAddress) = state
        return ScanningState.DevicesDiscovered(
            devices = devices
                .asSequence()
                .sortedWith(
                    compareBy<DiscoveredBluetoothDevice> { it.connectionState }
                        .thenBy { it.name != null }
                        .thenBy { -it.highestRssi }
                        .thenBy { it.address }
                )
                .filter { !config.filterNearbyOnly || it.highestRssi >= FILTER_RSSI }
                .filter { !config.filterWithNames || it.name != null }
                .map { device ->
                    if (device.address == currentMacAddress) {
                        device.copy(connectionState = currentState)
                    } else {
                        device
                    }
                }
                .toList()
        )
    }

    fun connect(device: DiscoveredBluetoothDevice) {
        launchOnIo(exceptionHandler) {
            repository.connectScooter(device.device)
        }
    }

    fun sendCommand() {
        launchOnIo(exceptionHandler) {
            repository.sendCommand(Ampere())
        }
    }

    fun setFilter(config: DevicesScanFilter) {
        filterConfig.update { config }
    }

    override fun onCleared() {
        super.onCleared()
        repository.release()
    }
}

data class DevicesScanFilter(
    val filterNearbyOnly: Boolean,
    val filterWithNames: Boolean,
)
