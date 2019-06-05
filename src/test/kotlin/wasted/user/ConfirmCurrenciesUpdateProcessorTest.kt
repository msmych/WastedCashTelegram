package wasted.user

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.User

internal class ConfirmCurrenciesUpdateProcessorTest {

    private val userClient = mock<UserClient>()
    private val bot = mock<TelegramLongPollingBot>()

    private val confirmCurrenciesUpdateProcessor = ConfirmCurrenciesUpdateProcessor()

    private val update = mock<Update>()
    private val callbackQuery = mock<CallbackQuery>()
    private val message = mock<Message>()
    private val user = mock<User>()

    @BeforeEach
    fun setUp() {
        confirmCurrenciesUpdateProcessor.userClient = userClient
        confirmCurrenciesUpdateProcessor.bot = bot
        whenever(update.callbackQuery).thenReturn(callbackQuery)
        whenever(callbackQuery.data).thenReturn("confirm-currencies")
        whenever(callbackQuery.message).thenReturn(message)
        whenever(callbackQuery.from).thenReturn(user)
    }

    @Test
    fun applies() {
        assertTrue(confirmCurrenciesUpdateProcessor.appliesTo(update))
    }

    @Test
    fun processing() {
        confirmCurrenciesUpdateProcessor.process(update)
        verify(userClient).getUserCurrencies(any())
        verify(bot).execute(any<EditMessageText>())
    }
}