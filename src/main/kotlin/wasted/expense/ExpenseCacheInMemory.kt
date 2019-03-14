package wasted.expense

import com.google.inject.Singleton
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Singleton
class ExpenseCacheInMemory : ExpenseCache {
    private val cache = ConcurrentHashMap<Int, ExpenseCacheItem>()

    override fun put(userId: Int, chatId: Long, currency: Currency, category: ExpenseCategory) {
        cache[userId] = ExpenseCacheItem(chatId, 0, currency, category)
    }

    override fun contains(userId: Int): Boolean {
        return cache.containsKey(userId)
    }

    override fun get(userId: Int): ExpenseCacheItem {
        return cache[userId] ?: throw IllegalArgumentException()
    }

    override fun remove(fromId: Int): ExpenseCacheItem {
        return cache.remove(fromId) ?: throw IllegalArgumentException()
    }

    override fun updateAmount(userId: Int, amount: Long): ExpenseCacheItem {
        val item = ExpenseCacheItem.updateAmount(cache[userId] ?: throw IllegalArgumentException(), amount)
        cache[userId] = item
        return item
    }

    override fun updateCurrency(userId: Int, currency: Currency): ExpenseCacheItem {
        val item = ExpenseCacheItem.updateCurrency(cache[userId] ?: throw IllegalArgumentException(), currency)
        cache[userId] = item
        return item
    }
}