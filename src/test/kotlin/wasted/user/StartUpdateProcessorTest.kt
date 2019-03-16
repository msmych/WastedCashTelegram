package wasted.user

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import wasted.rest.RestClient

internal class StartUpdateProcessorTest {

    private val restClient = mock<RestClient>()

    private val startUpdateProcessor = StartUpdateProcessor()

    private val update = mock<Update>()
    private val message = mock<Message>()

    @BeforeEach
    fun setUp() {
        startUpdateProcessor.restClient = restClient
        whenever(update.message).thenReturn(message)
        whenever(message.text).thenReturn("/start")
    }

    @Test
    fun applies() {
        assertTrue(startUpdateProcessor.appliesTo(update))
    }

    @Test
    fun processing() {
        startUpdateProcessor.process(update)
        verify(restClient).createUser(any())
    }
}