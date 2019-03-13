package wasted.expense

import com.google.inject.Singleton

@Singleton
class ExpenseCacheInMemory : ExpenseCache {

    private val cache = HashMap<Int, Item>()

    override fun put(userId: Int, chatId: Long) {
        cache[userId] = Item(chatId, 0)
    }

    private data class Item(val chatId: Long,
                            val amount: Long)
}