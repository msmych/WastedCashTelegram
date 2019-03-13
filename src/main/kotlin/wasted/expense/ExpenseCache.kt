package wasted.expense

interface ExpenseCache {

    fun put(userId: Int, chatId: Long)
}
