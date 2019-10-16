package wasted.total.report

import com.nhaarman.mockitokotlin2.*
import org.junit.Before
import org.junit.Test
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import wasted.expense.Expense.Category.*
import wasted.group.GroupClient
import wasted.keypad.TotalKeypad
import wasted.total.Total
import wasted.total.TotalClient

internal class MonthlyTotalReporterTest {

  private val groupClient = mock<GroupClient>()
  private val totalClient = mock<TotalClient>()
  private val bot = mock<TelegramLongPollingBot>()
  private val totalKeypad = TotalKeypad()

  private val reporter = MonthlyTotalReporter()

  private val totals = listOf(
    Total(1, 2, 1000, "USD", SHOPPING),
    Total(1, 3, 2000, "RUB", GROCERIES),
    Total(10, 11, 3000, "EUR", HOBBIES)
  )

  @Before
  fun setUp() {
    reporter.userId = "1234"
    reporter.groupClient = groupClient
    reporter.totalClient = totalClient
    reporter.totalKeypad = totalKeypad
    totalKeypad.bot = bot
    whenever(totalClient.total(any(), any(), any())).thenReturn(totals)
    whenever(groupClient.monthlyReportGroupsIds(any())).thenReturn(listOf(1, 10))
  }

  @Test
  fun reporting() {
    reporter.run()
    verify(bot, times(2)).execute(any<SendMessage>())
  }
}
