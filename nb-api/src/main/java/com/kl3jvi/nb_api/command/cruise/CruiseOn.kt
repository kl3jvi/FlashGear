package com.kl3jvi.nb_api.command.cruise

import com.kl3jvi.nb_api.command.Commands
import com.kl3jvi.nb_api.command.Message
import com.kl3jvi.nb_api.command.RawResponse
import com.kl3jvi.nb_api.command.RequestType
import com.kl3jvi.nb_api.command.ScooterCommand
import com.kl3jvi.nb_api.command.ScooterResponse

class CruiseOn : ScooterCommand() {
    override val tag: String = "Cruise"
    override val name: String = "Turn On Cruise"
    override val requestType: RequestType = RequestType.NOCOUNT

    override val requestBit: String = "7C"
    override val defaultUnit: String
        get() = TODO("Not yet implemented")

    override fun getRequestString(): String = Message.Builder()
        .setDirection(Commands.MASTER_TO_M365)
        .setRW(Commands.WRITE)
        .setPosition(0x7C)
        .setPayload(0x0001)
        .build()

    override fun handleResponse(deviceRawRawResponse: RawResponse): ScooterResponse {
        TODO("Not yet implemented")
    }

    fun handleResponse(request: List<String>): String {
        return request.toString()
    }
}
