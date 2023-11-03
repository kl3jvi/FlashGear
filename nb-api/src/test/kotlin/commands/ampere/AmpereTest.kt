package commands.ampere

import com.kl3jvi.nb_api.command.RawResponse
import com.kl3jvi.nb_api.command.ampere.Ampere
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class AmpereTest {
    private val rawValue: String = "e6070c10100015059401"
    private val rawResponse = RawResponse(rawValue, 1000)
    lateinit var ampere: Ampere

    @Before
    fun setup() {
        ampere = Ampere()
    }

    @Test
    fun testGetRequestString() {
        val requestString = ampere.getRequestString()
        assertEquals("55aa0322013302a4ff", requestString)
    }

    @Test
    fun testGetCurrentAmpere() {
        val currentAmpere = ampere.getCurrentAmpere("e6070c10100015059401")
        assertEquals(0.16, currentAmpere)
    }

    @Test
    fun testHandler() {
        // Call the handler property and pass it the RawResponse object
        val currentAmpere = ampere.handler(rawResponse)
        // Verify that the correct current ampere value is returned
        assertEquals("0.2", currentAmpere)
    }

    @Test
    fun `test valid ampere response handler`() {
        val scooterResponse =
            Ampere().run {
                handleResponse(rawResponse)
            }
        assertEquals("0.2A", scooterResponse.formattedValue)
    }
}
