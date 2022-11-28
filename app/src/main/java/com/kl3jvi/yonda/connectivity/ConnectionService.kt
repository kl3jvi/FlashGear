package com.kl3jvi.yonda.connectivity

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.polidea.rxandroidble3.RxBleClient
import com.polidea.rxandroidble3.scan.ScanSettings
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ConnectionService : KoinComponent, DefaultLifecycleObserver {
    private val rxBleClient: RxBleClient by inject()
    private val disposables = CompositeDisposable()

    /**
     * It scans for BLE devices.
     */
    @Synchronized
    fun scanBleDevices() = rxBleClient.scanBleDevices(
        ScanSettings.Builder()
            .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()
    ).subscribeOn(Schedulers.io())

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        disposables.dispose()
    }

    private fun Disposable.addToCollectiveDisposables() = apply {
        disposables.add(this)
    }
}
