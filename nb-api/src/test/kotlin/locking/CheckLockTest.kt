package locking

import com.kl3jvi.nb_api.command.RawResponse
import com.kl3jvi.nb_api.command.locking.CheckLock
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class CheckLockTest {
    private val rawValue: String = "123456678"
    private val rawResponse = RawResponse(rawValue, 1000)
    lateinit var checkLock: CheckLock

    @Before
    fun setup() {
        checkLock = CheckLock()
    }

    @Test
    fun testGetRequestString() {
        val requestString = checkLock.getRequestString()
        assertEquals("0055aa032001b20227ff", requestString)
    }

    @Test
    fun `test valid lock state response handler`() {
        val scooterResponse = CheckLock().run {
            handleResponse(rawResponse)
        }
        assertEquals("Unlocked", scooterResponse.formattedValue)
    }
}