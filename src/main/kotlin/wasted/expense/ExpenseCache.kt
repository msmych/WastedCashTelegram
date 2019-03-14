package wasted.expense

import java.util.*

interface ExpenseCache {

    fun put(userId: Int, chatId: Long, currency: Currency, category: String)
    fun contains(userId: Int): Boolean
    fun get(userId: Int): ExpenseCacheItem
    fun updateAmount(userId: Int, amount: Long): ExpenseCacheItem
}
