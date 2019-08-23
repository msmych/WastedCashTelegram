package wasted.settings

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update

internal class SettingsUpdateProcessorTest {

  private val bot = mock<TelegramLongPollingBot>()

  private val settingsUpdateProcessor = SettingsUpdateProcessor()

  private val update = mock<Update>()
  private val message = mock<Message>()

  @Before
  fun setUp() {
    settingsUpdateProcessor.bot = bot
    whenever(update.message).thenReturn(message)
    whenever(message.text).thenReturn("/settings")
  }

  @Test
  fun applies() {
    assertTrue(settingsUpdateProcessor.appliesTo(update))
  }

  @Test
  fun processing() {
    settingsUpdateProcessor.process(update)
    verify(bot).execute(any<SendMessage>())
  }
}
