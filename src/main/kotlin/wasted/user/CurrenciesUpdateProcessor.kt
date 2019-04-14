package wasted.user

import org.telegram.telegrambots.meta.api.objects.Update
import wasted.bot.update.processor.UpdateProcessor
import wasted.keypad.CurrenciesKeypad
import wasted.rest.RestClient
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrenciesUpdateProcessor : UpdateProcessor {

    @Inject lateinit var restClient: RestClient
    @Inject lateinit var currenciesKeypad: CurrenciesKeypad

    override fun appliesTo(update: Update): Boolean {
        val message = update.message ?: return false
        val text = message.text ?: return false
        return text == "/currencies"
    }

    override fun process(update: Update) {
        val userId = update.message.from.id
        if (!restClient.existsUser(userId))
            restClient.createUser(userId)
        currenciesKeypad.send(update.message.chatId, restClient.getUserCurrencies(userId))
    }
}