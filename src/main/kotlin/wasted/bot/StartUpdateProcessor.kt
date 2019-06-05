package wasted.bot

import com.google.inject.Inject
import com.google.inject.Singleton
import org.telegram.telegrambots.meta.api.objects.Update
import wasted.bot.update.processor.UpdateProcessor
import wasted.keypad.CurrenciesKeypad
import wasted.user.UserClient

@Singleton
class StartUpdateProcessor : UpdateProcessor {

    @Inject
    lateinit var userClient: UserClient
    @Inject
    lateinit var currenciesKeypad: CurrenciesKeypad

    override fun appliesTo(update: Update): Boolean {
        val message = update.message ?: return false
        val text = message.text ?: return false
        return text == "/start" && !userClient.existsUser(update.message.from.id)
    }

    override fun process(update: Update) {
        val fromId = update.message.from.id
        userClient.createUser(fromId)
        currenciesKeypad.send(update.message.chatId, userClient.getUserCurrencies(fromId))
    }
}