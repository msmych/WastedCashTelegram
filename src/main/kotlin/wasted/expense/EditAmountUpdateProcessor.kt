package wasted.expense

import com.google.inject.Inject
import com.google.inject.Singleton
import org.telegram.telegrambots.meta.api.objects.Update
import wasted.bot.update.processor.UpdateProcessor
import wasted.keypad.NumericKeypad

@Singleton
class EditAmountUpdateProcessor : UpdateProcessor {

    @Inject
    lateinit var expenseClient: ExpenseClient
    @Inject
    lateinit var numericKeypad: NumericKeypad

    override fun appliesTo(update: Update): Boolean {
        val callbackQuery = update.callbackQuery ?: return false
        val data = callbackQuery.data ?: return false
        return data == "edit-amount"
                && expenseClient.expenseByGroupIdAndTelegramMessageId(
            callbackQuery.message.chatId,
            callbackQuery.message.messageId)
            .userId == callbackQuery.from.id
    }

    override fun process(update: Update) {
        numericKeypad.update(expenseClient.expenseByGroupIdAndTelegramMessageId(
            update.callbackQuery.message.chatId,
            update.callbackQuery.message.messageId))
    }
}
