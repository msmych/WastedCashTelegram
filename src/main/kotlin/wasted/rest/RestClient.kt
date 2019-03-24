package wasted.rest

import wasted.expense.Expense
import java.util.*

interface RestClient  {

    fun existsUser(id: Int): Boolean
    fun createUser(userId: Int)
    fun getUserCurrencies(userId: Int): List<Currency>
    fun toggleCurrency(userId: Int, currency: String): List<Currency>
    fun getExpenseByGroupIdAndTelegramMessageId(groupId: Long, telegramMessageId: Int): Expense
    fun createExpense(request: CreateExpenseRequest): Expense
}