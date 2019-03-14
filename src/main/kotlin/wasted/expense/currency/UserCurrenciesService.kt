package wasted.expense.currency

import java.util.*

interface UserCurrenciesService {

    fun getCurrencies(userId: Int): List<Currency>
}