package wasted.bot

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.User
import wasted.keypad.CurrenciesKeypad
import wasted.rest.RestClient

internal class StartUpdateProcessorTest {

    private val restClient = mock<RestClient>()
    private val currenciesKeypad = CurrenciesKeypad()
    private val bot = mock<TelegramLongPollingBot>()

    private val startUpdateProcessor = StartUpdateProcessor()

    private val update = mock<Update>()
    private val message = mock<Message>()
    private val user = mock<User>()

    @BeforeEach
    fun setUp() {
        startUpdateProcessor.restClient = restClient
        startUpdateProcessor.currenciesKeypad = currenciesKeypad
        currenciesKeypad.bot = bot
        whenever(update.message).thenReturn(message)
        whenever(message.text).thenReturn("/start")
        whenever(message.from).thenReturn(user)
    }

    @Test
    fun applies() {
        assertTrue(startUpdateProcessor.appliesTo(update))
    }

    @Test
    fun existingUser() {
        whenever(restClient.existsUser(any())).thenReturn(true)
        assertFalse(startUpdateProcessor.appliesTo(update))
    }

    @Test
    fun processing() {
        startUpdateProcessor.process(update)
        verify(restClient).createUser(any())
        verify(bot).execute(any<SendMessage>())
    }
}