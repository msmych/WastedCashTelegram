package wasted.expense

import com.google.inject.Inject
import org.telegram.telegrambots.meta.api.objects.Update
import wasted.bot.update.processor.UpdateProcessor
import wasted.keypad.NumericKeypad
import wasted.rest.RestClient
import java.util.*

class AmountUpdateProcessor : UpdateProcessor {

    @Inject
    lateinit var restClient: RestClient
    @Inject
    lateinit var numericKeypad: NumericKeypad

    override fun appliesTo(update: Update): Boolean {
        val callbackQuery = update.callbackQuery ?: return false
        val data = callbackQuery.data ?: return false
        val fromId = callbackQuery.from.id
        val amount = data.toLongOrNull()
        if (data.length > 10 || amount == null)
            return false
        val expense = restClient.getExpenseByGroupIdAndTelegramMessageId(
            update.callbackQuery.message.chatId,
            update.callbackQuery.message.messageId)
        return expense.userId == fromId && expense.amount != amount
    }

    override fun process(update: Update) {
        val chatId = update.callbackQuery.message.chatId
        val messageId = update.callbackQuery.message.messageId
        val amount = update.callbackQuery.data.toLong()
        val expense = restClient.getExpenseByGroupIdAndTelegramMessageId(chatId, messageId)
        restClient.updateExpense(Expense(
            expense.id,
            expense.userId,
            expense.groupId,
            expense.telegramMessageId,
            amount,
            expense.currency,
            expense.category,
            expense.date))
        numericKeypad.update(chatId, messageId, amount, Currency.getInstance(expense.currency))
    }
}