package cruise

import com.kl3jvi.nb_api.command.Commands
import com.kl3jvi.nb_api.command.RawResponse
import com.kl3jvi.nb_api.command.RequestType
import com.kl3jvi.nb_api.command.ScooterMessage
import com.kl3jvi.nb_api.command.cruise.CheckCruise
import org.junit.Test
import kotlin.test.assertEquals

class CheckCruiseTest {
    private val checkCruise = CheckCruise()

    @Test
    fun testTag() {
        assertEquals("Cruise", checkCruise.tag)
    }

    @Test
    fun testName() {
        assertEquals("Check Cruise", checkCruise.name)
    }

    @Test
    fun testRequestBit() {
        assertEquals("7C", checkCruise.requestBit)
    }

    @Test
    fun testDefaultUnit() {
        assertEquals("", checkCruise.defaultUnit)
    }

    @Test
    fun testRequestType() {
        assertEquals(RequestType.CRUISE, checkCruise.requestType)
    }

    @Test
    fun testGetRequestString() {
        val expectedMessage = ScooterMessage()
            .setDirection(Commands.MASTER_TO_M365)
            .setReadOrWrite(Commands.READ)
            .setPosition(0x7C)
            .setPayload(0x02)
            .build()
        assertEquals(expectedMessage, checkCruise.getRequestString())
    }

    @Test
    fun testCruiseState_on() {
        val rawResponse = RawResponse("01", 1000L)
        assertEquals("ON", checkCruise.handler(rawResponse))
    }

    @Test
    fun testCruiseState_off() {
        val rawResponse = RawResponse("00", 1000L)
        assertEquals("OFF", checkCruise.handler(rawResponse))
    }
}
