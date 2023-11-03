package commands.locking

import com.kl3jvi.nb_api.command.Commands
import com.kl3jvi.nb_api.command.RequestType
import com.kl3jvi.nb_api.command.ScooterMessage
import com.kl3jvi.nb_api.command.locking.LockOff
import org.junit.Assert.*
import org.junit.Test

class LockOffTest {
    private val lockOff = LockOff()

    @Test
    fun testTag() {
        assertEquals("Lock", lockOff.tag)
    }

    @Test
    fun testName() {
        assertEquals("Remove Lock", lockOff.name)
    }

    @Test
    fun testRequestType() {
        assertEquals(RequestType.NO_COUNT, lockOff.requestType)
    }

    @Test
    fun testRequestBit() {
        assertEquals("71", lockOff.requestBit)
    }

    @Test
    fun testDefaultUnit() {
        assertEquals("", lockOff.defaultUnit)
    }

    @Test
    fun testGetRequestString() {
        val expectedMessage =
            ScooterMessage()
                .setDirection(Commands.MASTER_TO_M365)
                .setReadOrWrite(Commands.WRITE)
                .setPosition(0x7C)
                .setPayload(0x0000)
                .build()
        assertEquals(expectedMessage, lockOff.getRequestString())
    }
}
