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

internal class GroupSettingsKeypadTest {

  private val bot = mock<TelegramLongPollingBot>()
  private val botConfig = BotConfig()

  private val groupSettingsKeypad = GroupSettingsKeypad()

  @BeforeEach
  fun setUp() {
    groupSettingsKeypad.bot = bot
    groupSettingsKeypad.botConfig = botConfig
    botConfig.apiToken = "apiToken"
  }

  @Test
  fun sending() {
    groupSettingsKeypad.send(1, true)
    verify(bot).execute(any<SendMessage>())
  }

  @Test
  fun updating() {
    groupSettingsKeypad.update(1, 2, false)
    verify(bot).execute(any<EditMessageReplyMarkup>())
  }
}
