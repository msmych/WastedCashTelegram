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

internal class HelpUpdateProcessorTest {

    private val bot = mock<TelegramLongPollingBot>()

    private val helpUpdateProcessor = HelpUpdateProcessor()

    private var update = mock<Update>()
    private var message = mock<Message>()

    @BeforeEach
    fun setUp() {
        helpUpdateProcessor.bot = bot
        whenever(update.message).thenReturn(message)
    }

    @Test
    fun applies() {
        whenever(message.text).thenReturn("/help")
        assertTrue(helpUpdateProcessor.appliesTo(update))
    }

    @Test
    fun notApplies() {
        whenever(message.text).thenReturn("/wrong")
        assertFalse(helpUpdateProcessor.appliesTo(update))
    }

    @Test
    fun sending() {
        helpUpdateProcessor.process(update)
        verify(bot).execute(any<SendMessage>())
    }
}