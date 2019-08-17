package wasted.expense

import com.google.inject.Inject
import com.google.inject.Singleton
import org.telegram.telegrambots.meta.api.objects.Update
import wasted.bot.update.processor.UpdateProcessor
import wasted.expense.Expense.Category.Companion.fromName
import wasted.keypad.OptionsKeypad

@Singleton
class CategoryUpdateProcessor : UpdateProcessor {

    @Inject
    lateinit var expenseClient: ExpenseClient
    @Inject
    lateinit var optionsKeypad: OptionsKeypad

    override fun appliesTo(update: Update): Boolean {
        val callbackQuery = update.callbackQuery ?: return false
        val data = callbackQuery.data ?: return false
        return fromName(data) != null
                && expenseClient.expenseByGroupIdAndTelegramMessageId(
            callbackQuery.message.chatId,
            callbackQuery.message.messageId)
            .userId == callbackQuery.from.id
    }

    override fun process(update: Update) {
        val chatId = update.callbackQuery.message.chatId
        val messageId = update.callbackQuery.message.messageId
        val lastExpense = expenseClient.expenseByGroupIdAndTelegramMessageId(chatId, messageId)
        val expense = Expense(
            lastExpense.id,
            lastExpense.userId,
            lastExpense.groupId,
            lastExpense.telegramMessageId,
            lastExpense.amount,
            lastExpense.currency,
            Expense.Category.fromName(update.callbackQuery.data) ?: Expense.Category.OTHER,
            lastExpense.date)
        expenseClient.updateExpense(expense)
        optionsKeypad.update(expense)
    }
}
