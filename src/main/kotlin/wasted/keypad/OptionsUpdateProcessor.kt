package wasted.keypad

import com.google.inject.Inject
import com.google.inject.Singleton
import org.telegram.telegrambots.meta.api.objects.Update
import wasted.bot.update.processor.UpdateProcessor
import wasted.expense.ExpenseClient

@Singleton
class OptionsUpdateProcessor : UpdateProcessor {

    @Inject
    lateinit var expenseClient: ExpenseClient
    @Inject
    lateinit var optionsKeypad: OptionsKeypad

    override fun appliesTo(update: Update): Boolean {
        val callbackQuery = update.callbackQuery ?: return false
        val data = callbackQuery.data ?: return false
        return data == "options"
                && expenseClient.getExpenseByGroupIdAndTelegramMessageId(
            callbackQuery.message.chatId,
            callbackQuery.message.messageId)
            .userId == callbackQuery.from.id
    }

    override fun process(update: Update) {
        optionsKeypad.update(expenseClient.getExpenseByGroupIdAndTelegramMessageId(
            update.callbackQuery.message.chatId,
            update.callbackQuery.message.messageId))
    }
}