package wasted.expense

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.User
import wasted.expense.ExpenseCategory.SHOPPING
import wasted.rest.RestClient
import java.util.*

internal class RemoveExpenseUpdateProcessorTest {

    private val restClient = mock<RestClient>()
    private val bot = mock<TelegramLongPollingBot>()

    private val removeExpenseUpdateProcessor = RemoveExpenseUpdateProcessor()

    private val update = mock<Update>()

    private val callbackQuery = mock<CallbackQuery>()
    private val user = mock<User>()

    @BeforeEach
    fun setUp() {
        removeExpenseUpdateProcessor.restClient = restClient
        removeExpenseUpdateProcessor.bot = bot
        whenever(update.callbackQuery).thenReturn(callbackQuery)
        whenever(callbackQuery.data).thenReturn("remove-expense")
        whenever(callbackQuery.message).thenReturn(mock())
        whenever(callbackQuery.from).thenReturn(user)
        whenever(user.id).thenReturn(1)
        whenever(restClient.getExpenseByGroupIdAndTelegramMessageId(any(), any()))
            .thenReturn(Expense(1, 1, 2, 3, 1000, "USD", SHOPPING, Date()))
    }

    @Test
    fun applies() {
        assertTrue(removeExpenseUpdateProcessor.appliesTo(update))
    }

    @Test
    fun notOwnNotApplies() {
        whenever(restClient.getExpenseByGroupIdAndTelegramMessageId(any(), any()))
            .thenReturn(Expense(1, 111, 2, 3, 1000, "USD", SHOPPING, Date()))
        assertFalse(removeExpenseUpdateProcessor.appliesTo(update))
    }

    @Test
    fun processing() {
        removeExpenseUpdateProcessor.process(update)
        verify(restClient).removeExpenseByGroupIdAndTelegramMessageId(any(), any())
        verify(bot).execute(any<EditMessageText>())
    }
}