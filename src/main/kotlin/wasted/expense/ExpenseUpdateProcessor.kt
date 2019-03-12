package wasted.expense

import com.google.inject.Inject
import com.google.inject.Singleton
import org.telegram.telegrambots.meta.api.methods.ParseMode.MARKDOWN
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import wasted.bot.Bot
import wasted.bot.Emoji.*
import wasted.bot.ikb
import wasted.bot.update.processor.UpdateProcessor

@Singleton
class ExpenseUpdateProcessor : UpdateProcessor {

    @Inject
    lateinit var bot: Bot

    override fun appliesTo(update: Update): Boolean {
        val message = update.message ?: return false
        val text = message.text ?: return false
        return text == "/waste"
    }

    override fun process(update: Update) {
        bot.execute(SendMessage(update.message.chatId, "`0.00`")
            .setParseMode(MARKDOWN)
            .setReplyMarkup(InlineKeyboardMarkup()
                .setKeyboard(listOf(
                    listOf(ikb(ONE, "1"), ikb(TWO, "2"), ikb(THREE, "3")),
                    listOf(ikb(FOUR, "4"), ikb(FIVE, "5"), ikb(SIX, "6")),
                    listOf(ikb(SEVEN, "7"), ikb(EIGHT, "8"), ikb(NINE, "9")),
                    listOf(ikb("€", "eur"), ikb(ZERO, "0"), ikb(WHITE_CHECK_MARK, "ok"))
                ))))
    }
}