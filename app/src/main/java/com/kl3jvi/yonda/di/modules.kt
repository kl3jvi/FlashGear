package com.kl3jvi.yonda.di

import com.kl3jvi.yonda.connectivity.ConnectionService
import com.kl3jvi.yonda.ui.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val bleModule = module {
    single { ConnectionService(get()) }
}

val viewmodelModule = module {
    viewModel { HomeViewModel(get()) }
}
