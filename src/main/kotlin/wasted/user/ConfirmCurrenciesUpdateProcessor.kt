package wasted.user

import com.google.inject.Inject
import com.google.inject.Singleton
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.ParseMode.MARKDOWN
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.Update
import wasted.bot.update.processor.UpdateProcessor

@Singleton
class ConfirmCurrenciesUpdateProcessor : UpdateProcessor {

    @Inject
    lateinit var userClient: UserClient
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
            .setText("Your currencies are " +
                    userClient.userCurrencies(update.callbackQuery.from.id)
                        .joinToString(", ") { "*${it.symbol}*" })
            .setParseMode(MARKDOWN))
    }
}
