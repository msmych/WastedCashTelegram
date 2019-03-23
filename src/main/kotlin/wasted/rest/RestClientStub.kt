package wasted.rest

import wasted.expense.Expense
import wasted.expense.ExpenseCategory
import wasted.expense.ExpenseCategory.SHOPPING
import java.util.*
import java.util.stream.Stream
import kotlin.streams.toList

class RestClientStub : RestClient {

    private val currencies = Stream.of("USD", "EUR", "RUB")
        .map { Currency.getInstance(it) }
        .toList()

    override fun existsUser(id: Int): Boolean {
        return false
    }

    override fun createUser(userId: Int) {}

    override fun getUserCurrencies(userId: Int): List<Currency> {
        return currencies
    }

    override fun toggleCurrency(userId: Int, currency: String): List<Currency> {
        return currencies
    }

    override fun getExpense(userId: Int, chatId: Long, messageId: Int): Expense {
        return Expense(1, userId, chatId, messageId, 1000, "USD", SHOPPING, Date())
    }

    override fun saveExpense(userId: Int, chatId: Long, messageId: Int,
                             amount: Long, currency: Currency, category: ExpenseCategory) {}
}