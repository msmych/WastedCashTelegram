package wasted.expense.currency

import com.google.inject.Singleton
import java.util.*
import java.util.stream.Stream
import kotlin.streams.toList

@Singleton
class UserCurrenciesInMemory : UserCurrencies {

    override fun getCurrencies(userId: Int): List<Currency> {
        return Stream.of("USD", "EUR", "RUB")
            .map{ Currency.getInstance(it) }
            .toList()
    }
}