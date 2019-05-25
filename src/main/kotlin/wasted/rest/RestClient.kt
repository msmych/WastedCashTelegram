package wasted.rest

import wasted.expense.Expense
import wasted.expense.clear.ClearExpenseType
import java.util.*

interface RestClient  {

    fun existsUser(userId: Int): Boolean
    fun createUser(userId: Int)
    fun getUserCurrencies(userId: Int): List<Currency>
    fun toggleUserCurrency(userId: Int, currency: String): List<Currency>
    fun getExpenseByGroupIdAndTelegramMessageId(groupId: Long, telegramMessageId: Int): Expense
    fun createExpense(request: CreateExpenseRequest): Expense
    fun updateExpense(expense: Expense)
    fun removeExpenseByGroupIdAndTelegramMessageId(groupId: Long, telegramMessageId: Int)
    fun removeExpenseByType(groupId: Long, type: ClearExpenseType)
}