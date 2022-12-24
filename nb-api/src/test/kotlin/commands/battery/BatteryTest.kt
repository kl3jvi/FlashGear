package commands.battery

import com.kl3jvi.nb_api.command.Commands
import com.kl3jvi.nb_api.command.RawResponse
import com.kl3jvi.nb_api.command.RequestType
import com.kl3jvi.nb_api.command.ScooterMessage
import com.kl3jvi.nb_api.command.battery.Battery
import org.junit.Test
import kotlin.test.assertEquals

class BatteryTest {
    private val battery = Battery()

    @Test
    fun testTag() {
        assertEquals("Battery", battery.tag)
    }

    @Test
    fun testName() {
        assertEquals("Check Battery Life", battery.name)
    }

    @Test
    fun testRequestType() {
        assertEquals(RequestType.BATTERY_LIFE, battery.requestType)
    }

    @Test
    fun testRequestBit() {
        assertEquals("32", battery.requestBit)
    }

    @Test
    fun testGetRequestString() {
        val expectedMessage = ScooterMessage()
            .setDirection(Commands.MASTER_TO_BATTERY)
            .setReadOrWrite(Commands.READ)
            .setPosition(0x32)
            .setPayload(0x02)
            .build()

        assertEquals(expectedMessage, battery.getRequestString())
    }

    @Test
    fun testGetCurrentBattery() {
        val request = "abcdef01"
        val expectedResult = "1 %"
        val actualResult = battery.getCurrentBattery(request)

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun testHandler() {
        val rawResponse = RawResponse("abcdef01", 1000L)
        val expectedResult = "1 %"
        val actualResult = battery.handler(rawResponse)

        assertEquals(expectedResult, actualResult)
    }
}
