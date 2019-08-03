package wasted.total.report

import com.nhaarman.mockitokotlin2.mock
import org.junit.Before
import org.junit.Test
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import wasted.keypad.TotalKeypad
import wasted.total.TotalClient

internal class MonthlyTotalReporterTest {

    private val totalClient = mock<TotalClient>()
    private val bot = mock<TelegramLongPollingBot>()
    private val totalKeypad = TotalKeypad()

    private val reporter = MonthlyTotalReporter()

    @Before
    fun setUp() {
        reporter.totalClient = totalClient
        reporter.totalKeypad = totalKeypad
        totalKeypad.bot = bot
    }

    @Test
    fun reporting() {
        reporter.run()
    }
}