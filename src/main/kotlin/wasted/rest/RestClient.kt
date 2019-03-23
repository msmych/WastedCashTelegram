package wasted.rest

import wasted.expense.Expense
import wasted.expense.ExpenseCategory
import java.util.*

interface RestClient  {

    fun existsUser(id: Int): Boolean
    fun createUser(userId: Int)
    fun getUserCurrencies(userId: Int): List<Currency>
    fun getExpense(userId: Int, chatId: Long, messageId: Int): Expense
    fun saveExpense(userId: Int, chatId: Long, messageId: Int, amount: Long, currency: Currency, category: ExpenseCategory)
}