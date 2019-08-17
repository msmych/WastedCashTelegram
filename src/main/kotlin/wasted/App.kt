package wasted

import it.sauronsoftware.cron4j.Scheduler
import org.slf4j.LoggerFactory
import org.telegram.telegrambots.ApiContextInitializer.init
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.ApiContext.getInstance
import org.telegram.telegrambots.meta.ApiContext.register
import org.telegram.telegrambots.meta.TelegramBotsApi
import wasted.bot.Bot
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

fun main(args: Array<String>) {
  init()
  configure(args)
  startBot(args[0])
  startScheduler()
  LoggerFactory.getLogger("App").info("Поехали")
}

private fun configure(args: Array<String>) {
  register(TelegramLongPollingBot::class.java, Bot::class.java)
  if (args.size >= 2 && (args[1] == "--test" || args[1] == "--prod")) registerProd()
  else registerDev()
  if (args.size >= 3 && args[1] == "--test") configureProd(args[2])
  else if (args.size >= 2 && args[1] == "--prod") configureProd(args[0])
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

fun configureProd(apiToken: String) {
  (getInstance(UserClient::class.java) as UserRestClient).apiToken = apiToken
  (getInstance(ExpenseClient::class.java) as ExpenseRestClient).apiToken = apiToken
  (getInstance(TotalClient::class.java) as TotalRestClient).apiToken = apiToken
}

private fun startBot(token: String) {
  val bot = getInstance(Bot::class.java)
  bot.token = token
  bot.updateProcessors = updateProcessors
  TelegramBotsApi().registerBot(bot)
}

private fun startScheduler() {
  val scheduler = Scheduler()
  scheduler.schedule("0 9 1 * *", getInstance(MonthlyTotalReporter::class.java))
  scheduler.start()
}
