package com.kl3jvi.nb_api.command.version

import com.kl3jvi.nb_api.command.Commands
import com.kl3jvi.nb_api.command.RequestType
import com.kl3jvi.nb_api.command.ScooterCommand
import com.kl3jvi.nb_api.command.ScooterMessage

class CheckVersion : ScooterCommand() {
    override val tag: String
        get() = TODO("Not yet implemented")
    override val name: String
        get() = TODO("Not yet implemented")
    override val requestType: RequestType
        get() = TODO("Not yet implemented")
    override val requestBit: String
        get() = TODO("Not yet implemented")

    override fun getRequestString(): String =
        ScooterMessage()
            .setDirection(Commands.MASTER_TO_M365)
            .setReadOrWrite(Commands.READ)
            .setPosition(0x1A)
            .setPayload(0x02)
            .build()
}
