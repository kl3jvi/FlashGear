package com.kl3jvi.nb_api.command.battery

import com.kl3jvi.nb_api.command.Commands
import com.kl3jvi.nb_api.command.RawResponse
import com.kl3jvi.nb_api.command.RequestType
import com.kl3jvi.nb_api.command.ScooterCommand
import com.kl3jvi.nb_api.command.ScooterMessage

class Battery : ScooterCommand() {
    override val tag: String = "Battery"
    override val name: String = "Check Battery Life"
    override val requestType: RequestType = RequestType.BATTERY_LIFE
    override val requestBit: String = "32"

    override val handler = { it: RawResponse -> "%s".format(getCurrentBattery(it.result)) }

    override fun getRequestString(): String =
        ScooterMessage()
            .setDirection(Commands.MASTER_TO_BATTERY)
            .setReadOrWrite(Commands.READ)
            .setPosition(0x32)
            .setPayload(0x02)
            .build()

    fun getCurrentBattery(request: String): String {
        val temp = request.substring(6..7)
        val batteryLife: Int = temp.toInt(16)
        return "$batteryLife %"
    }
}
