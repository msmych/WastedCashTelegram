package wasted.rest

import com.google.inject.Singleton
import wasted.expense.Expense
import wasted.expense.Expense.Category.OTHER
import java.util.*
import java.util.concurrent.atomic.AtomicLong
import java.util.stream.Stream
import kotlin.collections.ArrayList
import kotlin.streams.toList

@Singleton
class RestClientStub : RestClient {

    private val expenseCounter = AtomicLong()
    private val expenses = ArrayList<Expense>()
    private val userCurrencies = HashMap<Int, ArrayList<Currency>>()

    private val currencies = Stream.of("USD", "EUR", "RUB")
        .map { Currency.getInstance(it) }
        .toList()

    override fun existsUser(userId: Int): Boolean {
        return userCurrencies.containsKey(userId)
    }

    override fun createUser(userId: Int) {
        userCurrencies[userId] = ArrayList(currencies)
    }

    override fun getUserCurrencies(userId: Int): List<Currency> {
        return userCurrencies[userId] ?: emptyList()
    }

    override fun toggleCurrency(userId: Int, currency: String): List<Currency> {
        if (!userCurrencies.containsKey(userId))
            userCurrencies[userId]?.add(Currency.getInstance(currency))
        else if (userCurrencies.size > 1)
            userCurrencies[userId]?.remove(Currency.getInstance(currency))
        return userCurrencies[userId] ?: emptyList()
    }

    override fun getExpenseByGroupIdAndTelegramMessageId(groupId: Long, telegramMessageId: Int): Expense {
        return expenses
            .find { it.groupId == groupId && it.telegramMessageId == telegramMessageId }
            ?: throw IllegalArgumentException()
    }

    override fun createExpense(request: CreateExpenseRequest): Expense {
        if (!userCurrencies.containsKey(request.userId))
            userCurrencies[request.userId] = ArrayList(currencies)
        val expense = Expense(
            expenseCounter.incrementAndGet(),
            request.userId,
            request.groupId,
            request.telegramMessageId,
            0,
            userCurrencies[request.userId]?.get(0)?.currencyCode ?: "USD",
            OTHER,
            Date())
        expenses.add(expense)
        return expense
    }

    override fun updateExpense(expense: Expense) {
        expenses[expenses.indexOfFirst { it.id == expense.id }] = expense
    }

    override fun removeExpenseByGroupIdAndTelegramMessageId(groupId: Long, telegramMessageId: Int) {
        expenses.remove(expenses.find { it.groupId == groupId && it.telegramMessageId == telegramMessageId })
    }
}