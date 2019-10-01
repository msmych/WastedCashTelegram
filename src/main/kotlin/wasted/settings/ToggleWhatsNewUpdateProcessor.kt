package wasted.settings

import org.telegram.telegrambots.meta.api.objects.Update
import wasted.bot.update.processor.UpdateProcessor
import wasted.keypad.UserSettingsKeypad
import wasted.user.UserClient
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ToggleWhatsNewUpdateProcessor : UpdateProcessor {

  @Inject
  lateinit var userClient: UserClient
  @Inject
  lateinit var userSettingsKeypad: UserSettingsKeypad

  override fun appliesTo(update: Update): Boolean {
    val callbackQuery = update.callbackQuery ?: return false
    val data = callbackQuery.data ?: return false
    return callbackQuery.from.id == callbackQuery.message.chatId.toInt()
      && data == "what's new"
  }

  override fun process(update: Update) {
    val callbackQuery = update.callbackQuery
    val message = callbackQuery.message
    userSettingsKeypad.update(
      message.chatId,
      message.messageId,
      userClient.toggleUserWhatsNew(callbackQuery.from.id))
  }
}
