package wasted.keypad

import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import wasted.bot.Emoji.WHITE_CHECK_MARK
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsKeypad {

  @Inject
  lateinit var bot: TelegramLongPollingBot

  fun send(chatId: Long, whatsNew: Boolean) {
    bot.execute(
      SendMessage(chatId, "Wasted settings")
        .setReplyMarkup(
          InlineKeyboardMarkup()
            .setKeyboard(
              listOf(
                listOf(
                  InlineKeyboardButton(if (whatsNew) "$WHITE_CHECK_MARK What's new" else "What's new")
                    .setCallbackData("what's new")
                )
              )
            )
        )
    )
  }
}
