package wasted.settings

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.User
import wasted.keypad.SettingsKeypad
import wasted.user.UserClient

internal class SettingsUpdateProcessorTest {

  private val bot = mock<TelegramLongPollingBot>()
  private val userClient = mock<UserClient>()
  private val settingsKeypad = SettingsKeypad()

  private val settingsUpdateProcessor = SettingsUpdateProcessor()

  private val update = mock<Update>()
  private val message = mock<Message>()
  private val user = mock<User>()

  @Before
  fun setUp() {
    settingsUpdateProcessor.bot = bot
    settingsUpdateProcessor.userClient = userClient
    settingsKeypad.bot = bot
    settingsUpdateProcessor.settingsKeypad = settingsKeypad
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
