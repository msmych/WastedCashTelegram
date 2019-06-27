package wasted.total

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import wasted.keypad.TotalKeypad

internal class TotalAllUpdateProcessorTest {

    private val bot = mock<TelegramLongPollingBot>()
    private val totalKeypad = TotalKeypad()
    private val totalClient = mock<TotalClient>()

    private val totalAllUpdateProcessor = TotalAllUpdateProcessor()

    private val update = mock<Update>()
    private val callbackQuery = mock<CallbackQuery>()
    private val message = mock<Message>()

    @BeforeEach
    fun setUp() {
        totalKeypad.bot = bot
        totalAllUpdateProcessor.totalClient = totalClient
        totalAllUpdateProcessor.totalKeypad = totalKeypad

        whenever(update.callbackQuery).thenReturn(callbackQuery)
        whenever(callbackQuery.message).thenReturn(message)
        whenever(callbackQuery.data).thenReturn("total_all")
    }

    @Test
    fun applies() {
        assertTrue(totalAllUpdateProcessor.appliesTo(update))
    }

    @Test
    fun processing() {
        totalAllUpdateProcessor.process(update)
        verify(totalClient).getTotal(any())
        verify(bot).execute(any<EditMessageText>())
    }
}