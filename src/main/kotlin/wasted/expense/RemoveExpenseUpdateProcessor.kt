package wasted.expense

import com.google.inject.Inject
import com.google.inject.Singleton
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import wasted.bot.Emoji.ARROW_LEFT
import wasted.bot.Emoji.X
import wasted.bot.update.processor.UpdateProcessor

@Singleton
class RemoveExpenseUpdateProcessor : UpdateProcessor {

    @Inject
    lateinit var expenseClient: ExpenseClient
    @Inject
    lateinit var bot: TelegramLongPollingBot

    override fun appliesTo(update: Update): Boolean {
        val callbackQuery = update.callbackQuery ?: return false
        val data = callbackQuery.data ?: return false
        val fromId = callbackQuery.from.id
        return data == "remove-expense"
                && expenseClient.expenseByGroupIdAndTelegramMessageId(
            callbackQuery.message.chatId,
            callbackQuery.message.messageId)
            .userId == fromId
    }

    override fun process(update: Update) {
        bot.execute(
            EditMessageText()
                .setChatId(update.callbackQuery.message.chatId)
                .setMessageId(update.callbackQuery.message.messageId)
                .setText("Are you sure want to remove the expense?")
                .setReplyMarkup(InlineKeyboardMarkup()
                    .setKeyboard(listOf(listOf(
                        InlineKeyboardButton("${ARROW_LEFT.code} Cancel").setCallbackData("cancel-expense-removal"),
                        InlineKeyboardButton("${X.code} Remove").setCallbackData("confirm-expense-removal"))))))
    }
}
