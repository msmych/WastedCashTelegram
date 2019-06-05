package wasted.user

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.User
import wasted.keypad.CurrenciesKeypad

internal class CurrenciesUpdateProcessorTest {

    private val userClient = mock<UserClient>()
    private val bot = mock<TelegramLongPollingBot>()
    private val currenciesKeypad = CurrenciesKeypad()

    private val currenciesUpdateProcessor = CurrenciesUpdateProcessor()

    private val update = mock<Update>()
    private val message = mock<Message>()
    private val user = mock<User>()

    @Before
    fun setUp() {
        currenciesUpdateProcessor.userClient = userClient
        currenciesKeypad.bot = bot
        currenciesUpdateProcessor.currenciesKeypad = currenciesKeypad

        whenever(update.message).thenReturn(message)
        whenever(message.text).thenReturn("/currencies")
        whenever(message.from).thenReturn(user)
    }

    @Test
    fun applies() {
        assertTrue(currenciesUpdateProcessor.appliesTo(update))
    }

    @Test
    fun processing() {
        currenciesUpdateProcessor.process(update)
        verify(userClient).createUser(any())
        verify(bot).execute(any<SendMessage>())
    }
}