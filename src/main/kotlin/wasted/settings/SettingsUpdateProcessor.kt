package wasted.settings

import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import wasted.bot.Emoji.WHITE_CHECK_MARK
import wasted.bot.update.processor.UpdateProcessor
import wasted.user.UserClient
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsUpdateProcessor : UpdateProcessor {

  @Inject
  lateinit var userClient: UserClient
  @Inject
  lateinit var bot: TelegramLongPollingBot

  override fun appliesTo(update: Update): Boolean {
    val message = update.message ?: return false
    val text = message.text ?: return false
    return message.from.id == message.chatId.toInt()
      && (text == "/settings" || text == "/settings@${bot.botUsername}")
  }

  override fun process(update: Update) {
    bot.execute(SendMessage(update.message.chatId, "Wasted settings")
      .setReplyMarkup(InlineKeyboardMarkup()
        .setKeyboard(listOf(listOf((
          InlineKeyboardButton(if (userClient.userWhatsNew(update.message.from.id)) "$WHITE_CHECK_MARK What's new" else "What's new")
            .setCallbackData("what's new")))))))
  }
}
