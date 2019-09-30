package wasted.settings

import org.telegram.telegrambots.meta.api.objects.Update
import wasted.bot.update.processor.UpdateProcessor
import wasted.keypad.SettingsKeypad
import wasted.user.UserClient
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ToggleMonthlyReportUpdateProcessor : UpdateProcessor {

  @Inject
  lateinit var userClient: UserClient
  @Inject
  lateinit var settingsKeypad: SettingsKeypad

  override fun appliesTo(update: Update): Boolean {
    val callbackQuery = update.callbackQuery ?: return false
    val data = callbackQuery.data ?: return false
    return callbackQuery.from.id == callbackQuery.message.chatId.toInt()
      && data == "monthly report"
  }

  override fun process(update: Update) {
    val callbackQuery = update.callbackQuery
    val message = callbackQuery.message
    val userId = callbackQuery.from.id
    settingsKeypad.update(
      message.chatId,
      message.messageId,
      userClient.toggleUserMonthlyReport(userId),
      userClient.userWhatsNew(userId)
    )
  }
}
