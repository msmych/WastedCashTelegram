package wasted.expense

import com.google.inject.Inject
import com.google.inject.Singleton
import org.telegram.telegrambots.meta.api.objects.Update
import wasted.bot.update.processor.UpdateProcessor
import wasted.keypad.OptionsKeypad
import wasted.user.UserClient
import java.util.*

@Singleton
class NextCurrencyOptionsUpdateProcessor : UpdateProcessor {

    @Inject
    lateinit var userClient: UserClient
    @Inject
    lateinit var expenseClient: ExpenseClient
    @Inject
    lateinit var optionsKeypad: OptionsKeypad

    override fun appliesTo(update: Update): Boolean {
        val callbackQuery = update.callbackQuery ?: return false
        val data = callbackQuery.data ?: return false
        val fromId = callbackQuery.from.id
        return data == "next-currency-option"
                && expenseClient.getExpenseByGroupIdAndTelegramMessageId(
            callbackQuery.message.chatId,
            callbackQuery.message.messageId)
            .userId == fromId &&
                userClient.getUserCurrencies(fromId).size > 1
    }

    override fun process(update: Update) {
        val fromId = update.callbackQuery.from.id
        val chatId = update.callbackQuery.message.chatId
        val messageId = update.callbackQuery.message.messageId
        val lastExpense = expenseClient.getExpenseByGroupIdAndTelegramMessageId(chatId, messageId)
        val currencies = userClient.getUserCurrencies(fromId)
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
        expenseClient.updateExpense(expense)
        optionsKeypad.update(expense)
    }
}