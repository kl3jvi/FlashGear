package connection

import com.kl3jvi.nb_api.command.RawResponse
import com.kl3jvi.nb_api.command.ScooterCommand
import com.kl3jvi.nb_api.command.ScooterResponse
import com.kl3jvi.nb_api.connection.DeviceConnection
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class DeviceConnectionTest {
    private val inputStream = mockk<ByteArrayInputStream>()
    private val outputStream = mockk<ByteArrayOutputStream>()
    private lateinit var deviceConnection: DeviceConnection

    @Before
    fun setup() {
        deviceConnection = DeviceConnection(inputStream, outputStream)
    }

    @Test
    fun `test run method`() = runBlocking {
        // Set up mock objects
        val command = mockk<ScooterCommand>()
        val rawResponse = RawResponse("raw data", 100)
        val scooterResponse = mockk<ScooterResponse>()
        every { command.handleResponse(rawResponse) } returns scooterResponse
        coEvery { deviceConnection.runCommand(command, 0) } returns rawResponse
        every { inputStream.available() } returns 10

        // Call the method being tested
        val result = deviceConnection.run(command)

        // Verify that the method behaves as expected
        verify { command.handleResponse(rawResponse) }
        assert(result == scooterResponse)
    }
}
