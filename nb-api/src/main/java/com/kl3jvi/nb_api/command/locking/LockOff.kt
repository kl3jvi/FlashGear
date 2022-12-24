package com.kl3jvi.nb_api.command.locking

import com.kl3jvi.nb_api.command.Commands
import com.kl3jvi.nb_api.command.RequestType
import com.kl3jvi.nb_api.command.ScooterCommand
import com.kl3jvi.nb_api.command.ScooterMessage

class LockOff : ScooterCommand() {
    override val tag: String = "Lock"
    override val name: String = "Remove Lock"
    override val requestType: RequestType = RequestType.NO_COUNT

    override val requestBit: String = "71"
    override val defaultUnit: String = ""

    override fun getRequestString(): String = ScooterMessage()
        .setDirection(Commands.MASTER_TO_M365)
        .setReadOrWrite(Commands.WRITE)
        .setPosition(0x7C)
        .setPayload(0x0000)
        .build()
}
