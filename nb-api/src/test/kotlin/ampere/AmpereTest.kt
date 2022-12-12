package ampere

import com.kl3jvi.nb_api.command.RawResponse
import com.kl3jvi.nb_api.command.ampere.Ampere
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class AmpereTest {

    private val rawValue: String = "12345678"
    private val rawResponse = RawResponse(rawValue, 1000)
    lateinit var ampere: Ampere

    @Before
    fun setup() {
        ampere = Ampere()
    }

    @Test
    fun testGetRequestString() {
        val requestString = ampere.getRequestString()
        assertEquals("0055aa0322013302a4ff", requestString)
    }

    @Test
    fun testGetCurrentAmpere() {
        val currentAmpere = ampere.getCurrentAmpere("12345678")
        assertEquals(1.2, currentAmpere)
    }

    @Test
    fun testHandler() {
        // Call the handler property and pass it the RawResponse object
        val currentAmpere = ampere.handler(rawResponse)
        // Verify that the correct current ampere value is returned
        assertEquals("1.2", currentAmpere)
    }

    @Test
    fun `test valid ampere response handler`() {
        val scooterResponse = Ampere().run {
            handleResponse(rawResponse)
        }
        assertEquals("1.2A", scooterResponse.formattedValue)
    }
}
