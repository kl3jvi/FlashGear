package com.kl3jvi.nb_api.command.cruise

import com.kl3jvi.nb_api.command.Commands
import com.kl3jvi.nb_api.command.Message
import com.kl3jvi.nb_api.command.RequestType
import com.kl3jvi.nb_api.command.ScooterCommand

class CheckCruise : ScooterCommand {
    override val tag: String = "Cruise"
    override val name: String = "Check Cruise"
    override val requestBit: String = "7C"
    override val requestType: RequestType = RequestType.CRUISE

    override fun getRequestString() = Message.Builder()
        .setDirection(Commands.MASTER_TO_M365)
        .setRW(Commands.READ)
        .setPosition(0x7C)
        .setPayload(0x02)
        .build()

    /**
     * If the 7th element of the list is equal to 01, return true, otherwise return false.
     *
     * @param request The request that was sent to the server.
     */
    fun handleResponse(request: List<String>): Boolean = request[6] == "01"
}
