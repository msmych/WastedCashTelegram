package wasted.keypad

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
import wasted.expense.Expense
import wasted.expense.Expense.Category.SHOPPING
import wasted.expense.ExpenseClient
import wasted.expense.OptionsUpdateProcessor
import java.time.Instant.now

internal class OptionsUpdateProcessorTest {

  private val expenseClient = mock<ExpenseClient>()
  private val optionsKeypad = OptionsKeypad()
  private val bot = mock<TelegramLongPollingBot>()

  private val optionsUpdateProcessor = OptionsUpdateProcessor()

  private val update = mock<Update>()
  private val callbackQuery = mock<CallbackQuery>()
  private val user = mock<User>()
  private val message = mock<Message>()

  @BeforeEach
  fun setUp() {
    optionsUpdateProcessor.expenseClient = expenseClient
    optionsUpdateProcessor.optionsKeypad = optionsKeypad
    optionsKeypad.bot = bot
    whenever(update.callbackQuery).thenReturn(callbackQuery)
    whenever(callbackQuery.data).thenReturn("options")
    whenever(callbackQuery.from).thenReturn(user)
    whenever(callbackQuery.message).thenReturn(message)
    whenever(user.id).thenReturn(2)
    whenever(expenseClient.expenseByGroupIdAndTelegramMessageId(any(), any(), any()))
      .thenReturn(Expense(1, 2, 3, 4, 1000, "USD", SHOPPING, now()))
  }

  @Test
  fun applies() {
    assertTrue(optionsUpdateProcessor.appliesTo(update))
  }

  @Test
  fun notOwnNotApplies() {
    whenever(expenseClient.expenseByGroupIdAndTelegramMessageId(any(), any(), any()))
      .thenReturn(Expense(1, 111, 3, 4, 1000, "USD", SHOPPING, now()))
    assertFalse(optionsUpdateProcessor.appliesTo(update))
  }

  @Test
  fun processing() {
    optionsUpdateProcessor.process(update)
    verify(bot).execute(any<EditMessageText>())
  }
}
