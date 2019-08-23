package wasted.settings

import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.objects.Update
import wasted.bot.update.processor.UpdateProcessor
import wasted.keypad.SettingsKeypad
import wasted.user.UserClient
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsUpdateProcessor : UpdateProcessor {

  @Inject
  lateinit var userClient: UserClient
  @Inject
  lateinit var bot: TelegramLongPollingBot
  @Inject
  lateinit var settingsKeypad: SettingsKeypad

  override fun appliesTo(update: Update): Boolean {
    val message = update.message ?: return false
    val text = message.text ?: return false
    return message.from.id == message.chatId.toInt()
      && (text == "/settings" || text == "/settings@${bot.botUsername}")
  }

  override fun process(update: Update) {
    settingsKeypad.send(update.message.chatId, userClient.userWhatsNew(update.message.from.id))
  }
}
