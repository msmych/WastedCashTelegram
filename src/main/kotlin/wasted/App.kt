package wasted

import org.slf4j.LoggerFactory
import org.telegram.telegrambots.ApiContextInitializer.init
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.ApiContext.getInstance
import org.telegram.telegrambots.meta.ApiContext.register
import org.telegram.telegrambots.meta.TelegramBotsApi
import wasted.bot.Bot
import wasted.bot.HelpUpdateProcessor
import wasted.bot.StartUpdateProcessor
import wasted.expense.*
import wasted.expense.clear.ClearUpdateProcessor
import wasted.keypad.OptionsUpdateProcessor
import wasted.rest.RestClient
import wasted.rest.RestClientStub
import wasted.rest.RestHttpClient
import wasted.user.ConfirmCurrenciesUpdateProcessor
import wasted.user.CurrenciesUpdateProcessor
import wasted.user.ToggleCurrencyUpdateProcessor

fun main(args: Array<String>) {
    init()
    register(TelegramLongPollingBot::class.java, Bot::class.java)
    if (args.size > 1 && args[1] == "--prod") configureProd()
    else configure()
    val bot = getInstance(Bot::class.java)
    bot.token = args[0]
    bot.addUpdateProcessor(
        getInstance(StartUpdateProcessor::class.java),
        getInstance(CurrenciesUpdateProcessor::class.java),
        getInstance(ToggleCurrencyUpdateProcessor::class.java),
        getInstance(ConfirmCurrenciesUpdateProcessor::class.java),
        getInstance(WastedUpdateProcessor::class.java),
        getInstance(AmountUpdateProcessor::class.java),
        getInstance(ExpenseUpdateProcessor::class.java),
        getInstance(EditAmountUpdateProcessor::class.java),
        getInstance(HelpUpdateProcessor::class.java),
        getInstance(NextCurrencyUpdateProcessor::class.java),
        getInstance(NextCurrencyOptionsUpdateProcessor::class.java),
        getInstance(RemoveExpenseUpdateProcessor::class.java),
        getInstance(CancelExpenseRemovalUpdateProcessor::class.java),
        getInstance(ConfirmExpenseRemovalUpdateProcessor::class.java),
        getInstance(EditCategoryUpdateProcessor::class.java),
        getInstance(CategoryUpdateProcessor::class.java),
        getInstance(OptionsUpdateProcessor::class.java),
        getInstance(ClearUpdateProcessor::class.java))
    TelegramBotsApi().registerBot(bot)
    LoggerFactory.getLogger("App").info("Поехали")
}

fun configure() {
    register(RestClient::class.java, RestClientStub::class.java)
}

fun configureProd() {
    register(RestClient::class.java, RestHttpClient::class.java)
}
