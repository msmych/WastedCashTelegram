package wasted.keypad

import com.google.inject.Inject
import com.google.inject.Singleton
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import wasted.bot.Emoji.BALLOT_BOX_WITH_CHECK
import wasted.bot.Emoji.WHITE_CHECK_MARK
import wasted.bot.ikb
import java.util.*
import java.util.stream.Stream
import kotlin.collections.ArrayList
import kotlin.streams.toList

@Singleton
class CurrenciesKeypad {

    companion object {
        val AVAILABLE_CURRENCIES = Stream.of(
            "USD", "EUR", "RUB",
            "GBP", "CHF", "JPY")
            .map { Currency.getInstance(it) }
            .toList()
    }

    @Inject
    lateinit var bot: TelegramLongPollingBot

    fun send(chatId: Long, currencies: List<Currency>) {
        bot.execute(SendMessage(chatId, "Your currencies")
            .setReplyMarkup(getMarkup(currencies)))
    }

    fun update(chatId: Long, messageId: Int, currencies: List<Currency>) {
        bot.execute(EditMessageReplyMarkup()
            .setChatId(chatId)
            .setMessageId(messageId)
            .setReplyMarkup(getMarkup(currencies)))
    }

    private fun getMarkup(currencies: List<Currency>): InlineKeyboardMarkup {
        val buttons = ArrayList<InlineKeyboardButton>()
        val availableCurrencies = AVAILABLE_CURRENCIES.groupBy { currencies.contains(it) }
        buttons.addAll(availableCurrencies[true]
            ?.map{ ikb("${BALLOT_BOX_WITH_CHECK.code} ${it.symbol}", it.currencyCode) } ?: emptyList())
        buttons.addAll(availableCurrencies[false]
            ?.map{ ikb(it.symbol, it.currencyCode) } ?: emptyList())
        val keyboard = ArrayList<List<InlineKeyboardButton>>()
        var index = 0
        while (index < buttons.size) {
            val row = ArrayList<InlineKeyboardButton>()
            row.add(buttons[index++])
            if (index < buttons.size)
                row.add(buttons[index++])
            if (index < buttons.size)
                row.add(buttons[index++])
            keyboard.add(row)
        }
        keyboard.add(listOf(ikb(WHITE_CHECK_MARK, "confirm-currencies")))
        return InlineKeyboardMarkup().setKeyboard(keyboard)
    }
}