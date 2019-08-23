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
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.User
import wasted.expense.Expense.Category.SHOPPING
import wasted.keypad.OptionsKeypad
import java.time.Instant.now

internal class CategoryUpdateProcessorTest {

    private val expenseClient = mock<ExpenseClient>()
    private val optionsKeypad = OptionsKeypad()
    private val bot = mock<TelegramLongPollingBot>()

    private val categoryUpdateProcessor = CategoryUpdateProcessor()

    private val update = mock<Update>()
    private val callbackQuery = mock<CallbackQuery>()
    private val user = mock<User>()
    private val message = mock<Message>()

    @BeforeEach
    fun setUp() {
        categoryUpdateProcessor.expenseClient = expenseClient
        categoryUpdateProcessor.optionsKeypad = optionsKeypad
        optionsKeypad.bot = bot
        whenever(update.callbackQuery).thenReturn(callbackQuery)
        whenever(callbackQuery.data).thenReturn("SHOPPING")
        whenever(callbackQuery.from).thenReturn(user)
        whenever(user.id).thenReturn(2)
        whenever(callbackQuery.message).thenReturn(message)
        whenever(expenseClient.expenseByGroupIdAndTelegramMessageId(any(), any()))
            .thenReturn(Expense(1, 2, 3, 4, 1000, "USD", SHOPPING, now()))
    }

    @Test
    fun applies() {
        assertTrue(categoryUpdateProcessor.appliesTo(update))
    }

    @Test
    fun notOwnNotApplies() {
        whenever(expenseClient.expenseByGroupIdAndTelegramMessageId(any(), any()))
            .thenReturn(Expense(1, 111, 2, 3, 1000, "USD", SHOPPING, now()))
        assertFalse(categoryUpdateProcessor.appliesTo(update))
    }

    @Test
    fun processing() {
        categoryUpdateProcessor.process(update)
        verify(expenseClient).updateExpense(any())
        verify(bot).execute(any<EditMessageText>())
    }
}
