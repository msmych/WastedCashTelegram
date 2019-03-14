package wasted.rest

import wasted.expense.ExpenseCategory
import java.util.*

class RestClientStub : RestClient {

    override fun saveExpense(userId: Int, chatId: Long, amount: Long, currency: Currency, category: ExpenseCategory) {}
}