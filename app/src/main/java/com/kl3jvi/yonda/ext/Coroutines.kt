package com.kl3jvi.yonda.ext

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import com.kl3jvi.yonda.ble.model.DiscoveredBluetoothDevice
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 * Launches a new coroutine with the provided block and repeats it until the fragment's view lifecycle reaches a state
 * equal to or greater than the specified `minActiveState`. This function is only valid when called from a fragment's
 * viewLifecycleOwner, and will throw an exception if not.
 *
 * @param minActiveState the minimum active state required for the block to be executed (defaults to [Lifecycle.State.STARTED])
 * @param block the block to execute as a coroutine
 * @return a [Job] representing the launched coroutine
 */
inline fun Fragment.launchAndRepeatWithViewLifecycle(
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline block: suspend CoroutineScope.() -> Unit,
): Job {
    return viewLifecycleOwner.lifecycleScope.launch {
        lifecycle.repeatOnLifecycle(minActiveState) {
            block()
        }
    }
}

/**
 * Returns a flow that delays the emission of each element in this flow by the specified number of milliseconds.
 *
 * @param timeMillis the number of milliseconds to delay the emission of each element
 * @return a flow that delays the emission of each element in this flow by the specified number of milliseconds
 */
fun <T> Flow<T>.delayEachFor(timeMillis: Long): Flow<T> = onEach { delay(timeMillis) }

fun MutableList<DiscoveredBluetoothDevice>.accumulateUniqueDevices(): List<DiscoveredBluetoothDevice> {
    return this.fold(
        mutableListOf(),
    ) { acc, value ->
        acc.find { it.address == value.address }?.apply {
            update(value.scanResult!!)
        } ?: acc.add(value)
        acc
    }
}

fun List<DiscoveredBluetoothDevice>.sortDevices(): List<DiscoveredBluetoothDevice> {
    return this.sortedWith(
        compareBy<DiscoveredBluetoothDevice> { it.name == null }
            .thenByDescending { it.rssi },
    )
}

private fun List<DiscoveredBluetoothDevice>.findDeviceByAddress(address: String): DiscoveredBluetoothDevice? {
    return this.find { it.address == address }
}

inline fun <T> Iterable<T>.forEachIterable(block: (T) -> Unit) {
    with(iterator()) {
        while (hasNext()) {
            block(next())
        }
    }
}

fun ViewModel.launchOnIo(
    errorHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, throwable -> throw throwable },
    block: suspend CoroutineScope.() -> Unit,
): Job {
    return viewModelScope.launch(Dispatchers.IO + errorHandler, block = block)
}
