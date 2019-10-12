package wasted.settings

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.User
import wasted.bot.BotConfig
import wasted.keypad.UserSettingsKeypad
import wasted.user.UserClient

internal class UserSettingsUpdateProcessorTest {

  private val bot = mock<TelegramLongPollingBot>()
  private val userClient = mock<UserClient>()
  private val userSettingsKeypad = UserSettingsKeypad()
  private val botConfig = BotConfig()

  private val settingsUpdateProcessor = UserSettingsUpdateProcessor()
  private val update = mock<Update>()
  private val message = mock<Message>()

  private val user = mock<User>()

  @BeforeEach
  fun setUp() {
    settingsUpdateProcessor.userClient = userClient
    userSettingsKeypad.bot = bot
    userSettingsKeypad.botConfig = botConfig
    botConfig.apiToken = "apiToken"
    botConfig.apiBaseUrl = "apiBaseUrl"
    settingsUpdateProcessor.userSettingsKeypad = userSettingsKeypad
    whenever(update.message).thenReturn(message)
    whenever(message.text).thenReturn("/settings")
    whenever(message.from).thenReturn(user)
    whenever(message.chatId).thenReturn(1)
    whenever(user.id).thenReturn(1)
  }

  @Test
  fun applies() {
    assertTrue(settingsUpdateProcessor.appliesTo(update))
  }

  @Test
  fun not_applies_in_group() {
    whenever(user.id).thenReturn(2)
    assertFalse(settingsUpdateProcessor.appliesTo(update))
  }

  @Test
  fun processing() {
    settingsUpdateProcessor.process(update)
    verify(bot).execute(any<SendMessage>())
  }
}
