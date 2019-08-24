package wasted.keypad

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Test
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup

internal class SettingsKeypadTest {

  private val bot = mock<TelegramLongPollingBot>()

  private val settingsKeypad = SettingsKeypad()

  @Before
  fun setUp() {
    settingsKeypad.bot = bot
  }

  @Test
  fun sending() {
    settingsKeypad.send(1, true)
    verify(bot).execute(any<SendMessage>())
  }

  @Test
  fun updating() {
    settingsKeypad.update(1, 2, false)
    verify(bot).execute(any<EditMessageReplyMarkup>())
  }
}
