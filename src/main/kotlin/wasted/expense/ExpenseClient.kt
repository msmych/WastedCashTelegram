package wasted.expense

import wasted.expense.clear.ClearExpenseType

interface ExpenseClient {

  fun expenseByGroupIdAndTelegramMessageId(groupId: Long, telegramMessageId: Int, userId: Int): Expense

  fun createExpense(request: CreateExpenseRequest, userId: Int): Expense

  fun updateExpense(expense: Expense, userId: Int)

  fun removeExpenseByGroupIdAndTelegramMessageId(groupId: Long, telegramMessageId: Int, userId: Int)

  fun removeExpenseByType(groupId: Long, type: ClearExpenseType, userId: Int)
}
