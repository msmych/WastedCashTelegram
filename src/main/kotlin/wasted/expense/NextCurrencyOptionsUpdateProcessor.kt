package wasted.expense

import com.google.inject.Inject
import com.google.inject.Singleton
import org.telegram.telegrambots.meta.api.objects.Update
import wasted.bot.update.processor.UpdateProcessor
import wasted.keypad.OptionsKeypad
import wasted.rest.RestClient
import java.util.*

@Singleton
class NextCurrencyOptionsUpdateProcessor : UpdateProcessor {

    @Inject
    lateinit var restClient: RestClient
    @Inject
    lateinit var optionsKeypad: OptionsKeypad

    override fun appliesTo(update: Update): Boolean {
        val callbackQuery = update.callbackQuery ?: return false
        val data = callbackQuery.data ?: return false
        return data == "next-currency-option"
                && restClient.getExpenseByGroupIdAndTelegramMessageId(
            callbackQuery.message.chatId,
            callbackQuery.message.messageId)
            .userId == callbackQuery.from.id
    }

    override fun process(update: Update) {
        val fromId = update.callbackQuery.from.id
        val chatId = update.callbackQuery.message.chatId
        val messageId = update.callbackQuery.message.messageId
        val lastExpense = restClient.getExpenseByGroupIdAndTelegramMessageId(chatId, messageId)
        val currencies = restClient.getUserCurrencies(fromId)
        val currency = currencies[(currencies.indexOf(Currency.getInstance(lastExpense.currency)) + 1) % currencies.size].currencyCode
        val expense = Expense(
            lastExpense.id,
            lastExpense.userId,
            lastExpense.groupId,
            lastExpense.telegramMessageId,
            lastExpense.amount,
            currency,
            lastExpense.category,
            lastExpense.date)
        restClient.updateExpense(expense)
        optionsKeypad.update(expense)
    }
}