package wasted.expense.clear

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.User
import wasted.expense.ExpenseClient

internal class ClearByTypeUpdateProcessorTest {

    private val expenseClient = mock<ExpenseClient>()
    private val bot = mock<TelegramLongPollingBot>()

    private val clearAllUpdateProcessor = ClearByTypeUpdateProcessor()

    private val update = mock<Update>()
    private val callbackQuery = mock<CallbackQuery>()
    private val message = mock<Message>()
    private val user = mock<User>()

    @BeforeEach
    fun setUp() {
        clearAllUpdateProcessor.expenseClient = expenseClient
        clearAllUpdateProcessor.bot = bot

        whenever(update.callbackQuery).thenReturn(callbackQuery)
        whenever(callbackQuery.data).thenReturn("clearALL")
        whenever(callbackQuery.message).thenReturn(message)
        whenever(message.from).thenReturn(user)
    }

    @Test
    fun applies() {
        assertTrue(clearAllUpdateProcessor.appliesTo(update))
    }

    @Test
    fun processing() {
        clearAllUpdateProcessor.process(update)
        verify(expenseClient).removeExpenseByType(any(), any())
    }
}