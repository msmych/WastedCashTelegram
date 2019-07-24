package wasted.stub

import com.google.inject.Inject
import com.google.inject.Singleton
import wasted.expense.CreateExpenseRequest
import wasted.expense.Expense
import wasted.expense.ExpenseClient
import wasted.expense.clear.ClearExpenseType
import wasted.expense.clear.ClearExpenseType.ALL
import wasted.expense.clear.ClearExpenseType.UP_TO_THIS_MONTH
import java.time.ZonedDateTime.now
import java.util.*
import kotlin.collections.ArrayList

@Singleton
class ExpenseClientStub : ExpenseClient {
    @Inject
    lateinit var ims: InMemoryStorage

    override fun getExpenseByGroupIdAndTelegramMessageId(groupId: Long, telegramMessageId: Int): Expense {
        return ims.expenses
            .find { it.groupId == groupId && it.telegramMessageId == telegramMessageId }
            ?: throw IllegalArgumentException()
    }

    override fun createExpense(request: CreateExpenseRequest): Expense {
        if (!ims.userCurrencies.containsKey(request.userId))
            ims.userCurrencies[request.userId] = ArrayList(ims.currencies)
        val expense = Expense(
            ims.expenseCounter.incrementAndGet(),
            request.userId,
            request.groupId,
            request.telegramMessageId,
            request.amount,
            ims.userCurrencies[request.userId]?.get(0)?.currencyCode ?: "USD",
            Expense.Category.OTHER,
            Date()
        )
        ims.expenses.add(expense)
        return expense
    }

    override fun updateExpense(expense: Expense) {
        ims.expenses[ims.expenses.indexOfFirst { it.id == expense.id }] = expense
    }

    override fun removeExpenseByGroupIdAndTelegramMessageId(groupId: Long, telegramMessageId: Int) {
        ims.expenses.remove(ims.expenses.find { it.groupId == groupId && it.telegramMessageId == telegramMessageId })
    }

    override fun removeExpenseByType(groupId: Long, type: ClearExpenseType) {
        when (type) {
            ALL -> ims.expenses.removeAll(ims.expenses.filter { it.groupId == groupId })
            UP_TO_THIS_MONTH -> ims.expenses.removeAll(ims.expenses
                .filter { it.groupId == groupId }
                .filter { it.date.before(Date.from(
                    now()
                    .withDayOfMonth(1)
                    .withHour(0)
                    .withMinute(0)
                    .withSecond(0)
                    .toInstant())) })
        }
    }
}