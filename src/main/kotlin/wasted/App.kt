package wasted

import org.telegram.telegrambots.ApiContextInitializer
import org.telegram.telegrambots.meta.ApiContext
import org.telegram.telegrambots.meta.TelegramBotsApi
import wasted.bot.Bot
import wasted.bot.HelpUpdateProcessor
import wasted.expense.ExpenseUpdateProcessor

fun main(args: Array<String>) {
    ApiContextInitializer.init()
    val bot = ApiContext.getInstance(Bot::class.java)
    bot.token = args[0]
    bot.addUpdateProcessor(
        ApiContext.getInstance(ExpenseUpdateProcessor::class.java),
        ApiContext.getInstance(HelpUpdateProcessor::class.java))
    TelegramBotsApi().registerBot(bot)
}
