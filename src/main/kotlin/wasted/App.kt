package wasted

import org.telegram.telegrambots.ApiContextInitializer
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.ApiContext
import org.telegram.telegrambots.meta.TelegramBotsApi
import wasted.bot.Bot
import wasted.bot.HelpUpdateProcessor
import wasted.expense.ExpenseCache
import wasted.expense.ExpenseCacheInMemory
import wasted.expense.WastedClickUpdateProcessor
import wasted.expense.WastedUpdateProcessor
import wasted.expense.currency.NextCurrencyUpdateProcessor
import wasted.expense.currency.UserCurrenciesService
import wasted.expense.currency.UserCurrenciesServiceInMemory

fun main(args: Array<String>) {
    ApiContextInitializer.init()
    ApiContext.register(TelegramLongPollingBot::class.java, Bot::class.java)
    ApiContext.register(ExpenseCache::class.java, ExpenseCacheInMemory::class.java)
    ApiContext.register(UserCurrenciesService::class.java, UserCurrenciesServiceInMemory::class.java)
    val bot = ApiContext.getInstance(Bot::class.java)
    bot.token = args[0]
    bot.addUpdateProcessor(
        ApiContext.getInstance(WastedUpdateProcessor::class.java),
        ApiContext.getInstance(WastedClickUpdateProcessor::class.java),
        ApiContext.getInstance(HelpUpdateProcessor::class.java),
        ApiContext.getInstance(NextCurrencyUpdateProcessor::class.java))
    TelegramBotsApi().registerBot(bot)
}
