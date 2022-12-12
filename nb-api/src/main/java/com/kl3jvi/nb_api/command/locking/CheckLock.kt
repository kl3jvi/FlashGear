package com.kl3jvi.nb_api.command.locking

import com.kl3jvi.nb_api.command.Commands
import com.kl3jvi.nb_api.command.Message
import com.kl3jvi.nb_api.command.RawResponse
import com.kl3jvi.nb_api.command.RequestType
import com.kl3jvi.nb_api.command.ScooterCommand

class CheckLock : ScooterCommand() {

    override val tag: String = "Lock"
    override val name: String = "Check Lock"
    override val requestType: RequestType = RequestType.LOCK
    override val requestBit: String = "B2"

    override val handler = { it: RawResponse -> lockState(it.result) }

    override fun getRequestString() = Message()
        .setDirection(Commands.MASTER_TO_M365)
        .setRW(Commands.READ)
        .setPosition(0xB2)
        .setPayload(0x02)
        .build()

    private fun lockState(request: String): String = when (request) {
        "02" -> "Locked"
        else -> "Unlocked"
    }
}