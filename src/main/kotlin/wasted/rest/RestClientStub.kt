package wasted.rest

import wasted.expense.Expense
import wasted.expense.ExpenseCategory
import wasted.expense.ExpenseCategory.SHOPPING
import java.util.*
import java.util.stream.Stream
import kotlin.streams.toList

class RestClientStub : RestClient {

    override fun existsUser(id: Int): Boolean {
        return false
    }

    override fun createUser(userId: Int) {}

    override fun getUserCurrencies(userId: Int): List<Currency> {
        return Stream.of("USD", "EUR", "RUB")
            .map { c -> Currency.getInstance(c) }
            .toList()
    }

    override fun getExpense(userId: Int, chatId: Long, messageId: Int): Expense {
        return Expense(1, userId, chatId, messageId, 1000, "USD", SHOPPING, Date())
    }

    override fun saveExpense(userId: Int, chatId: Long, messageId: Int,
                             amount: Long, currency: Currency, category: ExpenseCategory) {}
}