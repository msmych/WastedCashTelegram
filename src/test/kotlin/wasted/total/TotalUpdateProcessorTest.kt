package wasted.total

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import wasted.expense.Expense.Category.SHOPPING

internal class TotalUpdateProcessorTest {

    private val bot = mock<TelegramLongPollingBot>()
    private val totalClient = mock<TotalClient>()

    private val totalUpdateProcessor = TotalUpdateProcessor()

    private val update = mock<Update>()
    private val message = mock<Message>()

    @BeforeEach
    fun setUp() {
        totalUpdateProcessor.bot = bot
        totalUpdateProcessor.totalClient = totalClient
        whenever(update.message).thenReturn(message)
        whenever(message.text).thenReturn("/total")
        whenever(totalClient.getTotal(any())).thenReturn(listOf(Total(1, 1000, "USD", SHOPPING)))
    }

    @Test
    fun applies() {
        assertTrue(totalUpdateProcessor.appliesTo(update))
    }

    @Test
    fun processing() {
        totalUpdateProcessor.process(update)
        verify(totalClient).getTotal(any())
        verify(bot).execute(any<SendMessage>())
    }
}