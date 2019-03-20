package wasted.expense

import java.util.*

data class Expense(val id: Long,
                   val userId: Int,
                   val groupId: Long,
                   val telegramMessageId: Int?,
                   val amount: Long,
                   val currency: String,
                   val category: ExpenseCategory,
                   val date: Date)
