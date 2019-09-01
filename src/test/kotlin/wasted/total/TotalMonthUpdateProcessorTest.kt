package wasted.total

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
import wasted.expense.Expense.Category.SHOPPING
import wasted.keypad.TotalKeypad

internal class TotalMonthUpdateProcessorTest {

  private val bot = mock<TelegramLongPollingBot>()
  private val totalClient = mock<TotalClient>()
  private val totalKeypad = TotalKeypad()

  private val totalMonthUpdateProcessor = TotalMonthUpdateProcessor()

  private val update = mock<Update>()
  private val message = mock<Message>()

  @BeforeEach
  fun setUp() {
    totalMonthUpdateProcessor.bot = bot
    totalMonthUpdateProcessor.totalClient = totalClient
    totalKeypad.bot = bot
    totalMonthUpdateProcessor.totalKeypad = totalKeypad
    whenever(update.message).thenReturn(message)
    whenever(message.text).thenReturn("/total")
    whenever(totalClient.total(any(), any(), any())).thenReturn(listOf(Total(1, 2, 1000, "USD", SHOPPING)))
  }

  @Test
  fun applies() {
    assertTrue(totalMonthUpdateProcessor.appliesTo(update))
  }

  @Test
  fun processing() {
    totalMonthUpdateProcessor.process(update)
    verify(totalClient).total(any(), any(), any())
    verify(bot).execute(any<SendMessage>())
  }
}
