package com.kl3jvi.yonda.di

import com.kl3jvi.yonda.connectivity.ConnectionService
import com.kl3jvi.yonda.connectivity.ConnectionServiceImpl
import com.kl3jvi.yonda.ui.detail.DetailsViewModel
import com.kl3jvi.yonda.ui.home.HomeViewModel
import com.welie.blessed.BluetoothCentralManager
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private val bleModule = module {
    single { BluetoothCentralManager(get()) }
    single<ConnectionService> { ConnectionServiceImpl() }
}

private val viewmodelModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { DetailsViewModel(get()) }
}

val allModules = bleModule + viewmodelModule
