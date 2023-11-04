package com.kl3jvi.yonda.ble.repository

import com.kl3jvi.yonda.ble.spec.FlashGear

class FlashGearRepository(
    private val flashGear: FlashGear
) : FlashGear by flashGear {

    override fun release() {
        flashGear.release()
    }
}