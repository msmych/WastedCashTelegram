package wasted.expense

import com.google.inject.Inject
import com.google.inject.Singleton
import org.telegram.telegrambots.meta.api.objects.Update
import wasted.bot.update.processor.UpdateProcessor
import wasted.expense.ExpenseCategory.OTHER
import wasted.keypad.OptionsKeypad
import wasted.rest.RestClient

@Singleton
class CategoryUpdateProcessor : UpdateProcessor {

    @Inject
    lateinit var restClient: RestClient
    @Inject
    lateinit var optionsKeypad: OptionsKeypad

    override fun appliesTo(update: Update): Boolean {
        val callbackQuery = update.callbackQuery ?: return false
        val data = callbackQuery.data ?: return false
        return ExpenseCategory.fromName(data) != null
                && restClient.getExpenseByGroupIdAndTelegramMessageId(
            callbackQuery.message.chatId,
            callbackQuery.message.messageId)
            .userId == callbackQuery.from.id
    }

    override fun process(update: Update) {
        val chatId = update.callbackQuery.message.chatId
        val messageId = update.callbackQuery.message.messageId
        val lastExpense = restClient.getExpenseByGroupIdAndTelegramMessageId(chatId, messageId)
        val expense = Expense(
            lastExpense.id,
            lastExpense.userId,
            lastExpense.groupId,
            lastExpense.telegramMessageId,
            lastExpense.amount,
            lastExpense.currency,
            ExpenseCategory.fromName(update.callbackQuery.data) ?: OTHER,
            lastExpense.date)
        restClient.updateExpense(expense)
        optionsKeypad.update(expense)
    }
}