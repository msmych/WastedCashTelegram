package wasted.expense

import java.util.*

interface ExpenseCache {

    fun put(userId: Int, chatId: Long, currency: Currency, category: ExpenseCategory)
    fun contains(userId: Int): Boolean
    fun get(userId: Int): ExpenseCacheItem
    fun remove(fromId: Int)
    fun updateAmount(userId: Int, amount: Long): ExpenseCacheItem
    fun updateCurrency(userId: Int, currency: Currency): ExpenseCacheItem
}
