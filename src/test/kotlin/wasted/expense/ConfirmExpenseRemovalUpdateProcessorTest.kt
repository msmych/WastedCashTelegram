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
import wasted.expense.Expense.Category.SHOPPING
import java.time.Instant.now

internal class ConfirmExpenseRemovalUpdateProcessorTest {

  private val expenseClient = mock<ExpenseClient>()
  private val bot = mock<TelegramLongPollingBot>()

  private val confirmExpenseRemovalUpdateProcessor = ConfirmExpenseRemovalUpdateProcessor()

  private val update = mock<Update>()

  private val callbackQuery = mock<CallbackQuery>()
  private val user = mock<User>()

  @BeforeEach
  fun setUp() {
    confirmExpenseRemovalUpdateProcessor.expenseClient = expenseClient
    confirmExpenseRemovalUpdateProcessor.bot = bot
    whenever(update.callbackQuery).thenReturn(callbackQuery)
    whenever(callbackQuery.data).thenReturn("confirm-expense-removal")
    whenever(callbackQuery.message).thenReturn(mock())
    whenever(callbackQuery.from).thenReturn(user)
    whenever(user.id).thenReturn(1)
    whenever(expenseClient.expenseByGroupIdAndTelegramMessageId(any(), any(), any()))
      .thenReturn(Expense(1, 1, 2, 3, 1000, "USD", SHOPPING, now()))
  }

  @Test
  fun applies() {
    assertTrue(confirmExpenseRemovalUpdateProcessor.appliesTo(update))
  }

  @Test
  fun notOwnNotApplies() {
    whenever(expenseClient.expenseByGroupIdAndTelegramMessageId(any(), any(), any()))
      .thenReturn(Expense(1, 111, 2, 3, 1000, "USD", SHOPPING, now()))
    assertFalse(confirmExpenseRemovalUpdateProcessor.appliesTo(update))
  }

  @Test
  fun processing() {
    confirmExpenseRemovalUpdateProcessor.process(update)
    verify(expenseClient).removeExpenseByGroupIdAndTelegramMessageId(any(), any(), any())
    verify(bot).execute(any<EditMessageText>())
  }
}
