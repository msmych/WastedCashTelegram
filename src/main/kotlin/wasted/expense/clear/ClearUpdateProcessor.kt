package wasted.expense.clear

import com.google.inject.Inject
import com.google.inject.Singleton
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import wasted.bot.update.processor.UpdateProcessor

@Singleton
class ClearUpdateProcessor : UpdateProcessor {

    @Inject
    lateinit var bot: TelegramLongPollingBot

    override fun appliesTo(update: Update): Boolean {
        val message = update.message ?: return false
        val text = message.text ?: return false
        return text == "/clear" || text == "/clear@${bot.botUsername}"
    }

    override fun process(update: Update) {
        bot.execute(SendMessage(update.message.chatId, "Clearing types:")
            .setReplyMarkup(InlineKeyboardMarkup()
                .setKeyboard(ClearExpenseType.values()
                    .map {
                        listOf(InlineKeyboardButton()
                            .setText(it.label)
                            .setCallbackData("clear${it.name}"))
                    })))
    }
}