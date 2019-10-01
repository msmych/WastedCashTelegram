package wasted.keypad

import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import wasted.bot.BotConfig
import wasted.bot.Emoji
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroupSettingsKeypad {

  @Inject
  lateinit var bot: TelegramLongPollingBot
  @Inject
  lateinit var botConfig: BotConfig

  fun send(chatId: Long, monthlyReport: Boolean) {
    bot.execute(
      SendMessage(chatId, "Wasted settings")
        .setReplyMarkup(settingsMarkup(monthlyReport))
    )
  }

  private fun settingsMarkup(monthlyReport: Boolean): InlineKeyboardMarkup? {
    return InlineKeyboardMarkup()
      .setKeyboard(
        listOf(
          listOf(
            InlineKeyboardButton(if (monthlyReport) "${Emoji.WHITE_CHECK_MARK.code} Monthly report" else "Monthly report")
              .setCallbackData("monthly report")
          )
        )
      )
  }

  fun update(chatId: Long, messageId: Int, monthlyReport: Boolean) {
    bot.execute(
      EditMessageReplyMarkup()
        .setChatId(chatId)
        .setMessageId(messageId)
        .setReplyMarkup(settingsMarkup(monthlyReport))
    )
  }
}
