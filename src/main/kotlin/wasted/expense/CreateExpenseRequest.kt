package wasted.expense

class CreateExpenseRequest(val userId: Int,
                           val groupId: Long,
                           val telegramMessageId: Int,
                           val amount: Long = 0)