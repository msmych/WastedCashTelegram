package wasted.settings

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.User
import wasted.bot.BotConfig
import wasted.group.GroupClient
import wasted.keypad.GroupSettingsKeypad

internal class GroupSettingsUpdateProcessorTest {

  private val bot = mock<TelegramLongPollingBot>()
  private val groupClient = mock<GroupClient>()
  private val groupSettingsKeypad = GroupSettingsKeypad()
  private val botConfig = BotConfig()

  private val groupSettingsUpdateProcessor = GroupSettingsUpdateProcessor()

  private val update = mock<Update>()
  private val message = mock<Message>()
  private val user = mock<User>()

  @BeforeEach
  fun setUp() {
    groupSettingsUpdateProcessor.groupClient = groupClient
    groupSettingsUpdateProcessor.bot = bot
    groupSettingsKeypad.bot = bot
    groupSettingsKeypad.botConfig = botConfig
    botConfig.apiToken = "apiToken"
    botConfig.apiBaseUrl = "apiBaseUrl"
    groupSettingsUpdateProcessor.groupSettingsKeypad = groupSettingsKeypad
    whenever(update.message).thenReturn(message)
    whenever(message.text).thenReturn("/settings")
    whenever(message.from).thenReturn(user)
    whenever(user.id).thenReturn(1234)
    whenever(message.chatId).thenReturn(1234)
  }

  @Test
  fun applies() {
    assertTrue(groupSettingsUpdateProcessor.appliesTo(update))
  }

  @Test
  fun processing() {
    groupSettingsUpdateProcessor.process(update)
    verify(bot).execute(any<SendMessage>())
  }
}
