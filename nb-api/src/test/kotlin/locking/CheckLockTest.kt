package locking

import com.kl3jvi.nb_api.command.Commands
import com.kl3jvi.nb_api.command.RawResponse
import com.kl3jvi.nb_api.command.RequestType
import com.kl3jvi.nb_api.command.ScooterMessage
import com.kl3jvi.nb_api.command.locking.CheckLock
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals


class CheckLockTest {
    private val checkLock = CheckLock()

    @Test
    fun testTag() {
        assertEquals("Lock", checkLock.tag)
    }

    @Test
    fun testName() {
        assertEquals("Check Lock", checkLock.name)
    }

    @Test
    fun testRequestType() {
        assertEquals(RequestType.LOCK, checkLock.requestType)
    }

    @Test
    fun testRequestBit() {
        assertEquals("B2", checkLock.requestBit)
    }

    @Test
    fun testGetRequestString() {
        val expectedMessage = ScooterMessage()
            .setDirection(Commands.MASTER_TO_M365)
            .setReadOrWrite(Commands.READ)
            .setPosition(0xB2)
            .setPayload(0x02)
            .build()
        assertEquals(expectedMessage, checkLock.getRequestString())
    }

    @Test
    fun testLockState_locked() {
        val rawResponse = RawResponse("02", 1000L)
        assertEquals("Locked", checkLock.handler(rawResponse))
    }

    @Test
    fun testLockState_unlocked() {
        val rawResponse = RawResponse("00", 1000L)
        assertEquals("Unlocked", checkLock.handler(rawResponse))
    }
}
