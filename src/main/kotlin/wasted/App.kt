package wasted

import org.telegram.telegrambots.ApiContextInitializer.init
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.ApiContext.getInstance
import org.telegram.telegrambots.meta.ApiContext.register
import org.telegram.telegrambots.meta.TelegramBotsApi
import wasted.bot.Bot
import wasted.bot.HelpUpdateProcessor
import wasted.expense.*
import wasted.expense.currency.NextCurrencyUpdateProcessor
import wasted.expense.currency.UserCurrenciesService
import wasted.expense.currency.UserCurrenciesServiceInMemory

fun main(args: Array<String>) {
    init()
    register(TelegramLongPollingBot::class.java, Bot::class.java)
    register(ExpenseCache::class.java, ExpenseCacheInMemory::class.java)
    register(UserCurrenciesService::class.java, UserCurrenciesServiceInMemory::class.java)
    val bot = getInstance(Bot::class.java)
    bot.token = args[0]
    bot.addUpdateProcessor(
        getInstance(WastedUpdateProcessor::class.java),
        getInstance(WastedClickUpdateProcessor::class.java),
        getInstance(HelpUpdateProcessor::class.java),
        getInstance(NextCurrencyUpdateProcessor::class.java),
        getInstance(CancelUpdateProcessor::class.java)
    )
    TelegramBotsApi().registerBot(bot)
    System.out.println("Поехали")
}
