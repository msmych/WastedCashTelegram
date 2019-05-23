package wasted.expense.clear

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

internal class ClearUpdateProcessorTest {

    private val bot = mock<TelegramLongPollingBot>()

    private val clearUpdateProcessor = ClearUpdateProcessor()

    private val update = mock<Update>()
    private val message = mock<Message>()

    @BeforeEach
    fun setUp() {
        clearUpdateProcessor.bot = bot
        whenever(update.message).thenReturn(message)
        whenever(message.text).thenReturn("/clear")
    }

    @Test
    fun applies() {
        assertTrue(clearUpdateProcessor.appliesTo(update))
    }

    @Test
    fun processing() {
        clearUpdateProcessor.process(update)
        verify(bot).execute(any<SendMessage>())
    }
}