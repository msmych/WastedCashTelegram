package wasted.keypad

import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import wasted.bot.Config
import wasted.bot.Emoji.WHITE_CHECK_MARK
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsKeypad {

  @Inject
  lateinit var bot: TelegramLongPollingBot
  @Inject
  lateinit var config: Config

  fun send(chatId: Long, whatsNew: Boolean) {
    bot.execute(
      SendMessage(chatId, "Wasted settings")
        .setReplyMarkup(settingsMarkup(chatId.toInt(), whatsNew))
    )
  }

  private fun settingsMarkup(userId: Int, whatsNew: Boolean): InlineKeyboardMarkup? {
    return InlineKeyboardMarkup()
      .setKeyboard(
        listOf(
          listOf(
            InlineKeyboardButton(if (whatsNew) "${WHITE_CHECK_MARK.code} What's new" else "What's new")
              .setCallbackData("what's new")
          ),
          listOf(
            InlineKeyboardButton("Go web")
              .setUrl("http://wasted.cash/?userId=$userId&apiToken=${config.apiToken}")
          )
        )
      )
  }

  fun update(chatId: Long, messageId: Int, whatsNew: Boolean) {
    bot.execute(
      EditMessageReplyMarkup()
        .setChatId(chatId)
        .setMessageId(messageId)
        .setReplyMarkup(settingsMarkup(chatId.toInt(), whatsNew))
    )
  }
}
