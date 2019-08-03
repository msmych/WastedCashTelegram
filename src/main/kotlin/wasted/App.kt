package wasted

import it.sauronsoftware.cron4j.Scheduler
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
import wasted.expense.clear.ClearByTypeUpdateProcessor
import wasted.expense.clear.ClearUpdateProcessor
import wasted.stub.ExpenseClientStub
import wasted.stub.TotalClientStub
import wasted.stub.UserClientStub
import wasted.total.TotalClient
import wasted.total.TotalMonthUpdateProcessor
import wasted.total.TotalRestClient
import wasted.total.TotalUpdateProcessor
import wasted.total.report.MonthlyTotalReporter
import wasted.user.*

fun main(args: Array<String>) {
    init()
    register(TelegramLongPollingBot::class.java, Bot::class.java)
    if (args.size >= 2 && (args[1] == "--test" || args[1] == "--prod")) registerProd()
    else registerDev()
    if (args.size >= 3 && args[1] == "--test") configureProd(args[2])
    else if (args.size >= 2 && args[1] == "--prod") configureProd(args[0])
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
        getInstance(ClearUpdateProcessor::class.java),
        getInstance(ClearByTypeUpdateProcessor::class.java),
        getInstance(TotalMonthUpdateProcessor::class.java),
        getInstance(TotalUpdateProcessor::class.java))
    TelegramBotsApi().registerBot(bot)
    startScheduler()
    LoggerFactory.getLogger("App").info("Поехали")
}

fun registerDev() {
    register(UserClient::class.java, UserClientStub::class.java)
    register(ExpenseClient::class.java, ExpenseClientStub::class.java)
    register(TotalClient::class.java, TotalClientStub::class.java)
}

fun registerProd() {
    register(UserClient::class.java, UserRestClient::class.java)
    register(ExpenseClient::class.java, ExpenseRestClient::class.java)
    register(TotalClient::class.java, TotalRestClient::class.java)
}

fun configureProd(apiToken: String) {
    (getInstance(UserClient::class.java) as UserRestClient).apiToken = apiToken
    (getInstance(ExpenseClient::class.java) as ExpenseRestClient).apiToken = apiToken
    (getInstance(TotalClient::class.java) as TotalRestClient).apiToken = apiToken
}

private fun startScheduler() {
    val scheduler = Scheduler()
    scheduler.schedule("* * * * *", getInstance(MonthlyTotalReporter::class.java))
    scheduler.start()
}
