package com.kl3jvi.yonda.manager.providers

import no.nordicsemi.android.ble.observer.BondingObserver

interface BondStateProvider {
    fun getBondState(): Int?
    fun subscribeOnBondingState(observer: BondingObserver)
    fun unsubscribeBondingState(observer: BondingObserver)
}
