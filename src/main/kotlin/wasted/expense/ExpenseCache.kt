package wasted.expense

import wasted.expense.Expense.Category
import java.util.*

interface ExpenseCache {

    fun put(userId: Int, chatId: Long, currency: Currency, category: Category)
    fun contains(userId: Int): Boolean
    fun get(userId: Int): ExpenseCacheItem
    fun remove(fromId: Int): ExpenseCacheItem
    fun updateAmount(userId: Int, amount: Long): ExpenseCacheItem
    fun updateCurrency(userId: Int, currency: Currency): ExpenseCacheItem
}
