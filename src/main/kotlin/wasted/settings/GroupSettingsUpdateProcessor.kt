package wasted.settings

import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.objects.Update
import wasted.bot.update.processor.UpdateProcessor
import wasted.group.GroupClient
import wasted.keypad.GroupSettingsKeypad
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroupSettingsUpdateProcessor : UpdateProcessor {

  @Inject
  lateinit var groupClient: GroupClient
  @Inject
  lateinit var bot: TelegramLongPollingBot
  @Inject
  lateinit var groupSettingsKeypad: GroupSettingsKeypad

  override fun appliesTo(update: Update): Boolean {
    val message = update.message ?: return false
    val text = message.text ?: return false
    return text == "/settings" || text == "/settings@${bot.botUsername}"
  }

  override fun process(update: Update) {
    val chatId = update.message.chatId
    groupSettingsKeypad.send(chatId, groupClient.groupMonthlyReport(chatId, update.message.from.id))
  }
}
