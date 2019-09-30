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
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.User
import wasted.keypad.SettingsKeypad
import wasted.user.UserClient

internal class ToggleWhatsNewUpdateProcessorTest {

  private val bot = mock<TelegramLongPollingBot>()
  private val userClient = mock<UserClient>()
  private val settingsKeypad = SettingsKeypad()

  private val toggleWhatsNewUpdateProcessor = ToggleWhatsNewUpdateProcessor()

  private val update = mock<Update>()
  private val callbackQuery = mock<CallbackQuery>()
  private val message = mock<Message>()
  private val user = mock<User>()

  @Before
  fun setUp() {
    toggleWhatsNewUpdateProcessor.userClient = userClient
    settingsKeypad.bot = bot
    toggleWhatsNewUpdateProcessor.settingsKeypad = settingsKeypad

    whenever(update.callbackQuery).thenReturn(callbackQuery)
    whenever(callbackQuery.data).thenReturn("what's new")
    whenever(callbackQuery.message).thenReturn(message)
    whenever(callbackQuery.from).thenReturn(user)
    whenever(user.id).thenReturn(1234)
    whenever(message.chatId).thenReturn(1234)
  }

  @Test
  fun applies() {
    assertTrue(toggleWhatsNewUpdateProcessor.appliesTo(update))
  }

  @Test
  fun not_applies_to_group_chat() {
    whenever(message.chatId).thenReturn(777)
    assertFalse(toggleWhatsNewUpdateProcessor.appliesTo(update))
  }

  @Test
  fun processing() {
    toggleWhatsNewUpdateProcessor.process(update)
    verify(userClient).toggleUserWhatsNew(any())
    verify(bot).execute(any<EditMessageReplyMarkup>())
  }
}
