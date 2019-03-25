package wasted.expense

import wasted.expense.Expense.Category
import java.util.*

data class ExpenseCacheItem(val chatId: Long,
                            val amount: Long,
                            val currency: Currency,
                            val category: Category
) {

    companion object {

        fun updateAmount(item: ExpenseCacheItem, amount: Long): ExpenseCacheItem {
            return ExpenseCacheItem(item.chatId, amount, item.currency, item.category)
        }

        fun updateCurrency(item: ExpenseCacheItem, currency: Currency): ExpenseCacheItem {
            return ExpenseCacheItem(item.chatId, item.amount, currency, item.category)
        }
    }
}