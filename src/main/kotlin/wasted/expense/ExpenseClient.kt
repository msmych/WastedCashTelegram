package wasted.expense

import wasted.expense.clear.ClearExpenseType

interface ExpenseClient {

    fun expenseByGroupIdAndTelegramMessageId(groupId: Long, telegramMessageId: Int, userId: Int): Expense

    fun createExpense(request: CreateExpenseRequest): Expense

    fun updateExpense(expense: Expense)

    fun removeExpenseByGroupIdAndTelegramMessageId(groupId: Long, telegramMessageId: Int)
    fun removeExpenseByType(groupId: Long, type: ClearExpenseType)
}
