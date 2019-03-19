package wasted.rest

import wasted.expense.ExpenseCategory
import java.util.*

interface RestClient  {

    fun updateUser(userId: Int)
    fun getUserCurrencies(userId: Int): List<Currency>
    fun saveExpense(userId: Int, chatId: Long, amount: Long, currency: Currency, category: ExpenseCategory)
}