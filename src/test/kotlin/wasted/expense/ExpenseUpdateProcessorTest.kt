package wasted.expense

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update

internal class ExpenseUpdateProcessorTest {

    private val bot = mock(TelegramLongPollingBot::class.java)

    private val expenseUpdateProcessor = ExpenseUpdateProcessor()

    private val update = mock(Update::class.java)
    private val message = mock(Message::class.java)

    @BeforeEach
    fun setUp() {
        expenseUpdateProcessor.bot = bot
        `when`(update.message).thenReturn(message)
    }

    @Test
    fun applies() {
        `when`(message.text).thenReturn("/waste")
        assertTrue(expenseUpdateProcessor.appliesTo(update))
    }

    @Test
    fun notApplies() {
        `when`(message.text).thenReturn("/wrong")
        assertFalse(expenseUpdateProcessor.appliesTo(update))
    }

    @Test
    fun sending() {
        expenseUpdateProcessor.process(update)
        verify(bot).execute(isA(SendMessage::class.java))
    }
}