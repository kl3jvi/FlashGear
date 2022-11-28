package com.kl3jvi.yonda.ui.home

import androidx.lifecycle.ViewModel
import com.kl3jvi.yonda.connectivity.ConnectionService
import com.polidea.rxandroidble3.scan.ScanResult
import io.reactivex.rxjava3.core.Observable

class HomeViewModel(
    private val connectionService: ConnectionService
) : ViewModel() {

    fun scan(): Observable<ScanResult> = connectionService.scanBleDevices()
}
