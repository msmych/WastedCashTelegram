package wasted.expense

import wasted.expense.clear.ClearExpenseType

interface ExpenseClient {

    fun getExpenseByGroupIdAndTelegramMessageId(groupId: Long, telegramMessageId: Int): Expense

    fun createExpense(request: CreateExpenseRequest): Expense

    fun updateExpense(expense: Expense)

    fun removeExpenseByGroupIdAndTelegramMessageId(groupId: Long, telegramMessageId: Int)
    fun removeExpenseByType(groupId: Long, type: ClearExpenseType)
}