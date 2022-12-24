package com.kl3jvi.nb_api.command.cruise

import com.kl3jvi.nb_api.command.Commands
import com.kl3jvi.nb_api.command.RequestType
import com.kl3jvi.nb_api.command.ScooterCommand
import com.kl3jvi.nb_api.command.ScooterMessage

class CruiseOff : ScooterCommand() {
    override val tag: String = "Cruise"
    override val name: String = "Turn Off Cruise"
    override val requestType: RequestType = RequestType.NO_COUNT

    override val requestBit: String = "7C"
    override val defaultUnit: String
        get() = ""

    override fun getRequestString(): String = ScooterMessage()
        .setDirection(Commands.MASTER_TO_M365)
        .setReadOrWrite(Commands.WRITE)
        .setPosition(0x7C)
        .setPayload(0x0000)
        .build()
}
