package com.kl3jvi.nb_api.command.ampere

import com.kl3jvi.nb_api.command.Commands
import com.kl3jvi.nb_api.command.Message
import com.kl3jvi.nb_api.command.RawResponse
import com.kl3jvi.nb_api.command.RequestType
import com.kl3jvi.nb_api.command.ScooterCommand

class Ampere : ScooterCommand() {
    override val tag: String = "Ampere"
    override val name: String = "Current current"
    override val requestType: RequestType = RequestType.AMEPERE
    override val requestBit: String = "33"
    override val defaultUnit: String = "A"

    override val handler = { it: RawResponse -> "%.1f".format(getCurrentAmpere(it.result)) }

    override fun getRequestString(): String = Message.Builder()
        .setDirection(Commands.MASTER_TO_BATTERY)
        .setRW(Commands.READ)
        .setPosition(0x33)
        .setPayload(0x02)
        .build()

    /**
     * It converts the hexadecimal value of the current to a decimal value.
     *
     * @param result The string that is returned from the bluetooth device.
     * @return The current ampere value is being returned.
     */
    fun getCurrentAmpere(result: String): Double {
        val temp = result[7].toString().plus(result[6])
        val amps: Int = temp.toInt(16)
        var c = amps.toDouble()
        c /= 100
        return c
    }
}
