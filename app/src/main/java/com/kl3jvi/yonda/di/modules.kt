package com.kl3jvi.yonda.di

import com.kl3jvi.yonda.connectivity.ConnectionService
import com.kl3jvi.yonda.ui.home.HomeViewModel
import com.polidea.rxandroidble3.RxBleClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val bleModule = module {
    /* It's creating a singleton instance of RxBleClient. */
    single<RxBleClient> { RxBleClient.create(get()) }
    /* It's creating a singleton instance of ConnectionService. */
    single { ConnectionService() }
}

val viewmodelModule = module {
    viewModel { HomeViewModel(get()) }
}
