package wasted.expense.currency

import java.util.*

interface UserCurrencies {

    fun getCurrencies(userId: Int): List<Currency>
}