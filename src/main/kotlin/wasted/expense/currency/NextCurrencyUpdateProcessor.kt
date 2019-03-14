package wasted.expense.currency

import com.google.inject.Inject
import com.google.inject.Singleton
import org.telegram.telegrambots.meta.api.objects.Update
import wasted.bot.update.processor.UpdateProcessor
import wasted.expense.ExpenseCache
import wasted.keypad.NumericKeypad

@Singleton
class NextCurrencyUpdateProcessor : UpdateProcessor {

    @Inject
    lateinit var userCurrencies: UserCurrencies
    @Inject
    lateinit var expenseCache: ExpenseCache
    @Inject
    lateinit var numericKeypad: NumericKeypad

    override fun appliesTo(update: Update): Boolean {
        val callbackQuery = update.callbackQuery ?: return false
        val data = callbackQuery.data ?: return false
        val fromId = update.callbackQuery.from.id
        return data == "next_currency"
                && expenseCache.contains(fromId)
                && expenseCache.get(fromId).chatId == update.callbackQuery.message.chatId
    }

    override fun process(update: Update) {
        val fromId = update.callbackQuery.from.id
        val currencies = userCurrencies.getCurrencies(fromId)
        val item = expenseCache.updateCurrency(fromId,
            currencies[(currencies.indexOf(expenseCache.get(fromId).currency) + 1) % currencies.size])
        numericKeypad.update(
            update.callbackQuery.message.chatId,
            update.callbackQuery.message.messageId,
            item.amount,
            item.currency)
    }
}