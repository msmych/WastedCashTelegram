package wasted.keypad

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup
import wasted.bot.BotConfig

internal class UserSettingsKeypadTest {

  private val bot = mock<TelegramLongPollingBot>()
  private val botConfig = BotConfig()

  private val userSettingsKeypad = UserSettingsKeypad()

  @BeforeEach
  fun setUp() {
    userSettingsKeypad.bot = bot
    userSettingsKeypad.botConfig = botConfig
    botConfig.apiToken = "apiToken"
  }

  @Test
  fun sending() {
    userSettingsKeypad.send(1, true)
    verify(bot).execute(any<SendMessage>())
  }

  @Test
  fun updating() {
    userSettingsKeypad.update(1, 2, false)
    verify(bot).execute(any<EditMessageReplyMarkup>())
  }
}
