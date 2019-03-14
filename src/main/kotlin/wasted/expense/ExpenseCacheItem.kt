package wasted.expense

import java.util.*

data class ExpenseCacheItem(val chatId: Long,
                            val amount: Long,
                            val currency: Currency,
                            val category: ExpenseCategory) {

    companion object {
        fun updateAmount(item: ExpenseCacheItem, amount: Long): ExpenseCacheItem {
            return ExpenseCacheItem(item.chatId, amount, item.currency, item.category)
        }
    }
}