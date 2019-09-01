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
import wasted.keypad.NumericKeypad
import java.time.Instant.now

internal class EditAmountUpdateProcessorTest {

  private val expenseClient = mock<ExpenseClient>()
  private val numericKeypad = NumericKeypad()
  private val bot = mock<TelegramLongPollingBot>()

  private val editAmountUpdateProcessor = EditAmountUpdateProcessor()

  private val update = mock<Update>()
  private val callbackQuery = mock<CallbackQuery>()
  private val message = mock<Message>()
  private val user = mock<User>()

  @BeforeEach
  fun setUp() {
    editAmountUpdateProcessor.expenseClient = expenseClient
    editAmountUpdateProcessor.numericKeypad = numericKeypad
    numericKeypad.bot = bot
    whenever(update.callbackQuery).thenReturn(callbackQuery)
    whenever(callbackQuery.message).thenReturn(message)
    whenever(callbackQuery.from).thenReturn(user)
    whenever(user.id).thenReturn(2)
    whenever(callbackQuery.data).thenReturn("edit-amount")
    whenever(expenseClient.expenseByGroupIdAndTelegramMessageId(any(), any(), any()))
      .thenReturn(Expense(1, 2, 3, 4, 1000, "USD", SHOPPING, now()))
  }

  @Test
  fun applies() {
    assertTrue(editAmountUpdateProcessor.appliesTo(update))
  }

  @Test
  fun notOwnNotApplies() {
    whenever(expenseClient.expenseByGroupIdAndTelegramMessageId(any(), any(), any()))
      .thenReturn(Expense(1, 111, 3, 4, 1000, "USD", SHOPPING, now()))
    assertFalse(editAmountUpdateProcessor.appliesTo(update))
  }

  @Test
  fun processing() {
    editAmountUpdateProcessor.process(update)
    verify(bot).execute(any<EditMessageText>())
  }
}
