package wasted.stub

import com.google.inject.Singleton
import wasted.expense.Expense
import java.util.*
import java.util.concurrent.atomic.AtomicLong
import java.util.stream.Stream
import kotlin.collections.ArrayList
import kotlin.streams.toList

@Singleton
class InMemoryStorage {

    val expenseCounter = AtomicLong()

    val expenses = ArrayList<Expense>()
    val userCurrencies = HashMap<Int, ArrayList<Currency>>()
    val currencies = Stream.of("USD", "EUR", "RUB")
        .map { Currency.getInstance(it) }
        .toList()
}