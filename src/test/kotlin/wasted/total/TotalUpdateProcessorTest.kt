package wasted.total

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import wasted.keypad.TotalKeypad

internal class TotalUpdateProcessorTest {

  private val totalClient = mock<TotalClient>()
  private val totalKeypad = TotalKeypad()
  private val bot = mock<TelegramLongPollingBot>()

  private val totalUpdateProcessor = TotalUpdateProcessor()

  private val update = mock<Update>()
  private val callbackQuery = mock<CallbackQuery>()
  private val message = mock<Message>()

  @BeforeEach
  fun setUp() {
    totalKeypad.bot = bot
    totalUpdateProcessor.totalKeypad = totalKeypad
    totalUpdateProcessor.totalClient = totalClient

    whenever(update.callbackQuery).thenReturn(callbackQuery)
    whenever(callbackQuery.data).thenReturn("totalMONTH")
    whenever(callbackQuery.message).thenReturn(message)
  }

  @Test
  fun applies() {
    assertTrue(totalUpdateProcessor.appliesTo(update))
  }

  @Test
  fun processing() {
    totalUpdateProcessor.process(update)
    verify(totalClient).total(any(), any(), any())
    verify(bot).execute(any<EditMessageText>())
  }
}
