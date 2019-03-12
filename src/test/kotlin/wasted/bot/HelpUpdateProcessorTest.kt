package wasted.bot

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update

internal class HelpUpdateProcessorTest {

    private val bot = mock(TelegramLongPollingBot::class.java)

    private val helpUpdateProcessor = HelpUpdateProcessor()

    private var update = mock(Update::class.java)
    private var message = mock(Message::class.java)

    @BeforeEach
    fun setUp() {
        helpUpdateProcessor.bot = bot
        `when`(update.message).thenReturn(message)
    }

    @Test
    fun applies() {
        `when`(message.text).thenReturn("/help")
        assertTrue(helpUpdateProcessor.appliesTo(update))
    }

    @Test
    fun notApplies() {
        `when`(message.text).thenReturn("/wrong")
        assertFalse(helpUpdateProcessor.appliesTo(update))
    }

    @Test
    fun sending() {
        helpUpdateProcessor.process(update)
        verify(bot).execute(ArgumentMatchers.isA(SendMessage::class.java))
    }
}