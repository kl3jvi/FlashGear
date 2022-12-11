import com.kl3jvi.nb_api.command.RawResponse
import com.kl3jvi.nb_api.command.ampere.Ampere
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class AmpereTest {

    lateinit var ampere: Ampere

    @Before
    fun setup() {
        ampere = Ampere()
    }

    @Test
    fun testGetRequestString() {
        val requestString = ampere.getRequestString()
        assertEquals("a4ff", requestString)
    }

    @Test
    fun testGetCurrentAmpere() {
        val currentAmpere = ampere.getCurrentAmpere("123456678")
        assertEquals(1.18, currentAmpere)
    }

    private val rawResponse = RawResponse("0016093300F401", 1000)

    @Test
    fun testHandler() {
        // Call the handler property and pass it the RawResponse object
        val currentAmpere = ampere.handler(rawResponse)
        // Verify that the correct current ampere value is returned
        assertEquals("0.5", currentAmpere)
    }
}