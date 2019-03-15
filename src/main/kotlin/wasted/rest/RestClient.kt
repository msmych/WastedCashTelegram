package wasted.rest

import wasted.expense.ExpenseCategory
import java.util.*

interface RestClient  {

    fun saveExpense(userId: Int, chatId: Long, amount: Long, currency: Currency, category: ExpenseCategory)
    fun getUserCurrencies(userId: Int): List<Currency>
}