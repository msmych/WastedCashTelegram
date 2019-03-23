package wasted.user

import com.google.inject.Inject
import com.google.inject.Singleton
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.Update
import wasted.bot.update.processor.UpdateProcessor
import wasted.rest.RestClient

@Singleton
class ConfirmCurrenciesUpdateProcessor : UpdateProcessor {

    @Inject
    lateinit var restClient: RestClient
    @Inject
    lateinit var bot: TelegramLongPollingBot

    override fun appliesTo(update: Update): Boolean {
        val callbackQuery = update.callbackQuery ?: return false
        val data = callbackQuery.data ?: return false
        return data == "confirm-currencies"
    }

    override fun process(update: Update) {
        bot.execute(EditMessageText()
            .setChatId(update.callbackQuery.message.chatId)
            .setMessageId(update.callbackQuery.message.messageId)
            .setText("Your currencies: " +
                    restClient.getUserCurrencies(update.callbackQuery.from.id)
                        .map { it.symbol }
                        .joinToString(", ")))
    }
}
