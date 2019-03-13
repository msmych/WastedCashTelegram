package wasted.expense

interface ExpenseCache {

    fun put(userId: Int, chatId: Long, currency: String, category: String)
    fun contains(userId: Int): Boolean
    fun get(userId: Int): ExpenseCacheItem
    fun updateAmount(userId: Int, amount: Long): ExpenseCacheItem
}
