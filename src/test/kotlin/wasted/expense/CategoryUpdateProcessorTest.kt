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
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.User
import wasted.expense.ExpenseCategory.SHOPPING
import wasted.rest.RestClient
import java.util.*

internal class CategoryUpdateProcessorTest {

    private val restClient = mock<RestClient>()
    private val bot = mock<TelegramLongPollingBot>()

    private val categoryUpdateProcessor = CategoryUpdateProcessor()

    private val update = mock<Update>()
    private val callbackQuery = mock<CallbackQuery>()
    private val user = mock<User>()
    private val message = mock<Message>()

    @BeforeEach
    fun setUp() {
        categoryUpdateProcessor.restClient = restClient
        categoryUpdateProcessor.bot = bot
        whenever(update.callbackQuery).thenReturn(callbackQuery)
        whenever(callbackQuery.data).thenReturn("SHOPPING")
        whenever(callbackQuery.from).thenReturn(user)
        whenever(callbackQuery.message).thenReturn(message)
    }

    @Test
    fun applies() {
        assertTrue(categoryUpdateProcessor.appliesTo(update))
    }

    @Test
    fun processing() {
        whenever(restClient.getExpenseByGroupIdAndTelegramMessageId(any(), any()))
            .thenReturn(Expense(1, 2, 3, 4, 1000, "USD", SHOPPING, Date()))
        categoryUpdateProcessor.process(update)
        verify(restClient).createExpense(any())
        verify(bot).execute(any<EditMessageText>())
    }
}