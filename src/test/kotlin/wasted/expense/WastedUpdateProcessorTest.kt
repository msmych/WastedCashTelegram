package wasted.expense

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import wasted.keypad.NumericKeypad
import wasted.rest.RestClient
import wasted.user.UserService
import java.util.*

internal class WastedUpdateProcessorTest {

    private val bot = mock<TelegramLongPollingBot>()
    private val numericKeypad = NumericKeypad()
    private val expenseCache = mock<ExpenseCache>()
    private val userService = UserService()
    private val restClient = mock<RestClient>()

    private val expenseUpdateProcessor = WastedUpdateProcessor()

    private val update = mock<Update>()
    private val message = mock<Message>()

    @BeforeEach
    fun setUp() {
        numericKeypad.bot = bot
        expenseUpdateProcessor.numericKeypad = numericKeypad
        expenseUpdateProcessor.expenseCache = expenseCache
        expenseUpdateProcessor.userService = userService
        userService.restClient = restClient
        whenever(update.message).thenReturn(message)
        whenever(message.from).thenReturn(mock())
    }

    @Test
    fun applies() {
        whenever(message.text).thenReturn("/wasted")
        assertTrue(expenseUpdateProcessor.appliesTo(update))
    }

    @Test
    fun notApplies() {
        whenever(message.text).thenReturn("/wrong")
        assertFalse(expenseUpdateProcessor.appliesTo(update))
    }

    @Test
    fun sending() {
        whenever(restClient.getUserCurrencies(any()))
            .thenReturn(listOf(Currency.getInstance("USD")))
        expenseUpdateProcessor.process(update)
        verify(expenseCache).put(any(), any(), any(), any())
    }
}