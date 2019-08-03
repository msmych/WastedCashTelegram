package wasted.total.report

import com.nhaarman.mockitokotlin2.*
import org.junit.Before
import org.junit.Test
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import wasted.expense.Expense.Category.*
import wasted.keypad.TotalKeypad
import wasted.total.Total
import wasted.total.TotalClient

internal class MonthlyTotalReporterTest {

    private val totalClient = mock<TotalClient>()
    private val bot = mock<TelegramLongPollingBot>()
    private val totalKeypad = TotalKeypad()

    private val reporter = MonthlyTotalReporter()

    private val totals = listOf(
        Total(1, 2, 1000, "USD", SHOPPING),
        Total(1, 3, 2000, "RUB", GROCERIES),
        Total(10, 11, 3000, "EUR", HOBBIES))

    @Before
    fun setUp() {
        reporter.totalClient = totalClient
        reporter.totalKeypad = totalKeypad
        totalKeypad.bot = bot

        whenever(totalClient.totals(any()))
            .thenReturn(totals)
    }

    @Test
    fun reporting() {
        reporter.run()
        verify(bot, times(2)).execute(any<SendMessage>())
    }
}