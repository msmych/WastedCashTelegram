package wasted.user

import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.objects.Update
import wasted.bot.update.processor.UpdateProcessor
import wasted.keypad.CurrenciesKeypad
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrenciesUpdateProcessor : UpdateProcessor {

    @Inject lateinit var bot: TelegramLongPollingBot
    @Inject lateinit var userClient: UserClient
    @Inject lateinit var currenciesKeypad: CurrenciesKeypad

    override fun appliesTo(update: Update): Boolean {
        val message = update.message ?: return false
        val text = message.text ?: return false
        return text == "/currencies" || text == "/currencies@${bot.botUsername}"
    }

    override fun process(update: Update) {
        val userId = update.message.from.id
        if (!userClient.existsUser(userId))
            userClient.createUser(userId)
        currenciesKeypad.send(update.message.chatId, userClient.getUserCurrencies(userId))
    }
}