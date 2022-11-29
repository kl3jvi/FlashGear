package com.kl3jvi.yonda.ui.home

import androidx.lifecycle.ViewModel
import com.kl3jvi.yonda.connectivity.ConnectionService

class HomeViewModel(
    private val connectionService: ConnectionService
) : ViewModel() {

    fun scan() = connectionService.scanBleDevices()
}
