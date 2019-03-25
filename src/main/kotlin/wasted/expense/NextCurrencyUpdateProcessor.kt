package wasted.expense

import com.google.inject.Inject
import com.google.inject.Singleton
import org.telegram.telegrambots.meta.api.objects.Update
import wasted.bot.update.processor.UpdateProcessor
import wasted.keypad.NumericKeypad
import wasted.rest.RestClient
import java.util.*

@Singleton
class NextCurrencyUpdateProcessor : UpdateProcessor {

    @Inject
    lateinit var restClient: RestClient
    @Inject
    lateinit var numericKeypad: NumericKeypad

    override fun appliesTo(update: Update): Boolean {
        val callbackQuery = update.callbackQuery ?: return false
        val data = callbackQuery.data ?: return false
        val fromId = update.callbackQuery.from.id
        return data == "next-currency"
                && restClient.getExpenseByGroupIdAndTelegramMessageId(
            callbackQuery.message.chatId,
            callbackQuery.message.messageId)
            .userId == fromId
                && restClient.getUserCurrencies(fromId).size > 1
    }

    override fun process(update: Update) {
        val fromId = update.callbackQuery.from.id
        val chatId = update.callbackQuery.message.chatId
        val messageId = update.callbackQuery.message.messageId
        val currencies = restClient.getUserCurrencies(fromId)
        val expense = restClient.getExpenseByGroupIdAndTelegramMessageId(chatId, messageId)
        val currency = currencies[(currencies.indexOf(Currency.getInstance(expense.currency)) + 1) % currencies.size].currencyCode
        restClient.updateExpense(Expense(
            expense.id,
            expense.userId,
            expense.groupId,
            expense.telegramMessageId,
            expense.amount,
            currency,
            expense.category,
            expense.date))
        numericKeypad.update(chatId, messageId, expense.amount, Currency.getInstance(currency))
    }
}