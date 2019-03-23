package wasted.user

import com.google.inject.Inject
import com.google.inject.Singleton
import org.telegram.telegrambots.meta.api.objects.Update
import wasted.bot.update.processor.UpdateProcessor
import wasted.keypad.CurrenciesKeypad
import wasted.keypad.CurrenciesKeypad.Companion.AVAILABLE_CURRENCIES
import wasted.rest.RestClient

@Singleton
class ToggleCurrencyUpdateProcessor : UpdateProcessor {

    @Inject
    lateinit var currenciesKeypad: CurrenciesKeypad
    @Inject
    lateinit var restClient: RestClient

    override fun appliesTo(update: Update): Boolean {
        val callbackQuery = update.callbackQuery ?: return false
        val data = callbackQuery.data ?: return false
        return AVAILABLE_CURRENCIES.any { it.currencyCode == data }
    }

    override fun process(update: Update) {
        currenciesKeypad.update(update.callbackQuery.message.chatId, update.callbackQuery.message.messageId,
            restClient.getUserCurrencies(update.callbackQuery.from.id))
    }
}