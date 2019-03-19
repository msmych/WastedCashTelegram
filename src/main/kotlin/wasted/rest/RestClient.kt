package wasted.rest

import wasted.expense.ExpenseCategory
import java.util.*

interface RestClient  {

    fun createUser(userId: Int)
    fun getUserCurrencies(userId: Int): List<Currency>
    fun saveExpense(userId: Int, chatId: Long, messageId: Int, amount: Long, currency: Currency, category: ExpenseCategory)
}