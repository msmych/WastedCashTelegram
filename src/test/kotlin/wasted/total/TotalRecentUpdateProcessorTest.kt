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

internal class TotalRecentUpdateProcessorTest {

    private val totalClient = mock<TotalClient>()
    private val totalKeypad = TotalKeypad()
    private val bot = mock<TelegramLongPollingBot>()

    private val totalRecentUpdateProcessor = TotalRecentUpdateProcessor()

    private val update = mock<Update>()
    private val callbackQuery = mock<CallbackQuery>()
    private val message = mock<Message>()

    @BeforeEach
    fun setUp() {
        totalKeypad.bot = bot
        totalRecentUpdateProcessor.totalKeypad = totalKeypad
        totalRecentUpdateProcessor.totalClient = totalClient

        whenever(update.callbackQuery).thenReturn(callbackQuery)
        whenever(callbackQuery.data).thenReturn("total_month")
        whenever(callbackQuery.message).thenReturn(message)
    }

    @Test
    fun applies() {
        assertTrue(totalRecentUpdateProcessor.appliesTo(update))
    }

    @Test
    fun processing() {
        totalRecentUpdateProcessor.process(update)
        verify(totalClient).getRecentTotal(any(), any())
        verify(bot).execute(any<EditMessageText>())
    }
}