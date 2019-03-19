package wasted.rest

import wasted.expense.ExpenseCategory
import java.util.*
import java.util.stream.Collectors
import java.util.stream.Collectors.*
import java.util.stream.Stream
import kotlin.streams.toList

class RestClientStub : RestClient {

    override fun updateUser(userId: Int) {}

    override fun getUserCurrencies(userId: Int): List<Currency> {
        return Stream.of("USD", "EUR", "RUB")
            .map { c -> Currency.getInstance(c) }
            .toList()
    }

    override fun saveExpense(userId: Int, chatId: Long, amount: Long, currency: Currency, category: ExpenseCategory) {}
}