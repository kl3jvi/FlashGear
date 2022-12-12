package cruise

import com.kl3jvi.nb_api.command.RawResponse
import com.kl3jvi.nb_api.command.cruise.CheckCruise
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class CruiseCheckTest {
    private val rawValue: String = "123456678"
    private val rawResponse = RawResponse(rawValue, 1000)
    lateinit var checkCruise: CheckCruise

    @Before
    fun setup() {
        checkCruise = CheckCruise()
    }

    @Test
    fun testGetRequestString() {
        val requestString = checkCruise.getRequestString()
        assertEquals("0055aa0320017c025dff", requestString)
    }

    @Test
    fun `test valid ampere response handler`() {
        val scooterResponse = CheckCruise().run {
            handleResponse(rawResponse)
        }
        assertEquals("OFF", scooterResponse.formattedValue)
    }
}
