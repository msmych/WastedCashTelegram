package wasted.expense

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
import org.telegram.telegrambots.meta.api.objects.Update

internal class CancelUpdateProcessorTest {

    private val expenseCache = mock<ExpenseCache>()
    private val bot = mock<TelegramLongPollingBot>()

    private val cancelUpdateProcessor = CancelUpdateProcessor()

    private val update = mock<Update>()

    private val callbackQuery = mock<CallbackQuery>()

    @BeforeEach
    fun setUp() {
        cancelUpdateProcessor.expenseCache = expenseCache
        cancelUpdateProcessor.bot = bot
        whenever(update.callbackQuery).thenReturn(callbackQuery)
        whenever(callbackQuery.data).thenReturn("cancel")
        whenever(callbackQuery.message).thenReturn(mock())
        whenever(callbackQuery.from).thenReturn(mock())
    }

    @Test
    fun applies() {
        assertTrue(cancelUpdateProcessor.appliesTo(update))
    }

    @Test
    fun processing() {
        cancelUpdateProcessor.process(update)
        verify(expenseCache).remove(any())
        verify(bot).execute(any<EditMessageText>())
    }
}