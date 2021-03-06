package wasted

import it.sauronsoftware.cron4j.Scheduler
import org.slf4j.LoggerFactory
import org.telegram.telegrambots.ApiContextInitializer.init
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.ApiContext.getInstance
import org.telegram.telegrambots.meta.ApiContext.register
import org.telegram.telegrambots.meta.TelegramBotsApi
import wasted.bot.Bot
import wasted.bot.BotConfig
import wasted.bot.update.processor.updateProcessors
import wasted.expense.ExpenseClient
import wasted.expense.ExpenseRestClient
import wasted.stub.ExpenseClientStub
import wasted.stub.TotalClientStub
import wasted.stub.UserClientStub
import wasted.total.TotalClient
import wasted.total.TotalRestClient
import wasted.total.report.MonthlyTotalReporter
import wasted.user.UserClient
import wasted.user.UserRestClient
import wasted.user.WhatsNewNotifier

fun main(args: Array<String>) {
  init()
  configure(args)
  startBot(args)
  startScheduler(args)
  LoggerFactory.getLogger("App").info("Поехали")
}

private fun configure(args: Array<String>) {
  register(TelegramLongPollingBot::class.java, Bot::class.java)
  if (args.any { it == "--test" || it == "--prod" }) registerProd()
  else registerDev()
  if (args.any { it == "--test" }) configureTest(args)
  else if (args.any { it == "--prod" }) configureProd(args)
}

fun registerProd() {
  register(UserClient::class.java, UserRestClient::class.java)
  register(ExpenseClient::class.java, ExpenseRestClient::class.java)
  register(TotalClient::class.java, TotalRestClient::class.java)
}

fun registerDev() {
  register(UserClient::class.java, UserClientStub::class.java)
  register(ExpenseClient::class.java, ExpenseClientStub::class.java)
  register(TotalClient::class.java, TotalClientStub::class.java)
}

private fun configureTest(args: Array<String>) {
  val botConfig = getInstance(BotConfig::class.java)
  botConfig.apiBaseUrl = "http://localhost:8080"
  botConfig.apiToken = args[2]
}

private fun configureProd(args: Array<String>) {
  val botConfig = getInstance(BotConfig::class.java)
  botConfig.apiBaseUrl = "https://wasted.cash"
  botConfig.apiToken = args[0]
}

private fun startBot(args: Array<String>) {
  val bot = getInstance(Bot::class.java)
  bot.token = args[0]
  bot.updateProcessors = updateProcessors
  TelegramBotsApi().registerBot(bot)
  maybeSendWhatsNew(args)
}

private fun maybeSendWhatsNew(args: Array<String>) {
  if (argPresent(args, "--whats-new"))
    getInstance(WhatsNewNotifier::class.java)
      .send(
        findArgEq(args, "--admin-id")?.toInt()
          ?: throw IllegalArgumentException("--admin-id is not present")
      )
}

private fun startScheduler(args: Array<String>) {
  val scheduler = Scheduler()
  val reporter = getInstance(MonthlyTotalReporter::class.java)
  reporter.userId = findArgEq(args, "--admin-id")
    ?: throw IllegalArgumentException("--admin-id is not present")
  scheduler.schedule("0 9 1 * *", reporter)
  scheduler.start()
}
