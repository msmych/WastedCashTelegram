package wasted.settings

import org.telegram.telegrambots.meta.api.objects.Update
import wasted.bot.update.processor.UpdateProcessor
import wasted.keypad.UserSettingsKeypad
import wasted.user.UserClient
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserSettingsUpdateProcessor : UpdateProcessor {

  @Inject
  lateinit var userClient: UserClient
  @Inject
  lateinit var userSettingsKeypad: UserSettingsKeypad

  override fun appliesTo(update: Update): Boolean {
    val message = update.message ?: return false
    val text = message.text ?: return false
    return message.from.id.toLong() == message.chatId && text == "/settings"
  }

  override fun process(update: Update) {
    userSettingsKeypad.send(update.message.chatId, userClient.userWhatsNew(update.message.from.id))
  }
}
