package com.kl3jvi.nb_api.command.cruise

import com.kl3jvi.nb_api.command.Commands
import com.kl3jvi.nb_api.command.ScooterMessage
import com.kl3jvi.nb_api.command.RawResponse
import com.kl3jvi.nb_api.command.RequestType
import com.kl3jvi.nb_api.command.ScooterCommand

class CheckCruise : ScooterCommand() {
    override val tag: String = "Cruise"
    override val name: String = "Check Cruise"
    override val requestBit: String = "7C"
    override val defaultUnit: String = ""

    override val requestType: RequestType = RequestType.CRUISE

    override val handler = { it: RawResponse -> cruiseState(it.result) }

    override fun getRequestString() = ScooterMessage()
        .setDirection(Commands.MASTER_TO_M365)
        .setReadOrWrite(Commands.READ)
        .setPosition(0x7C)
        .setPayload(0x02)
        .build()


    /**
     * If the request is 01, return ON, otherwise return Off.
     *
     * @param request The request string from the client.
     */
    private fun cruiseState(request: String): String = if (request == ("01")) "ON" else "OFF"
}
