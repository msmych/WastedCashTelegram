package wasted.expense

import com.google.inject.Singleton
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Singleton
class ExpenseCacheInMemory : ExpenseCache {
    private val cache = ConcurrentHashMap<Int, ExpenseCacheItem>()

    override fun put(userId: Int, chatId: Long, currency: Currency, category: String) {
        cache[userId] = ExpenseCacheItem(chatId, 0, currency, category)
    }

    override fun contains(userId: Int): Boolean {
        return cache.containsKey(userId)
    }

    override fun updateAmount(userId: Int, amount: Long): ExpenseCacheItem {
        val item = ExpenseCacheItem.updateAmount(cache[userId] ?: throw IllegalArgumentException(), amount)
        cache[userId] = item
        return item
    }

    override fun get(userId: Int): ExpenseCacheItem {
        return cache[userId] ?: throw IllegalArgumentException()
    }
}