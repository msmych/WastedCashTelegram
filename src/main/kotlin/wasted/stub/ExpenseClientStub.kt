package wasted.stub

import com.google.inject.Inject
import com.google.inject.Singleton
import wasted.expense.CreateExpenseRequest
import wasted.expense.Expense
import wasted.expense.ExpenseClient
import wasted.expense.clear.ClearExpenseType
import wasted.expense.clear.ClearExpenseType.ALL
import wasted.expense.clear.ClearExpenseType.UP_TO_THIS_MONTH
import wasted.user.User
import java.time.Instant
import java.time.ZonedDateTime

@Singleton
class ExpenseClientStub : ExpenseClient {
  @Inject
  lateinit var ims: InMemoryStorage

  override fun expenseByGroupIdAndTelegramMessageId(groupId: Long, telegramMessageId: Int, userId: Int): Expense {
    return ims.expenses
      .find { it.groupId == groupId && it.telegramMessageId == telegramMessageId }
      ?: throw IllegalArgumentException()
  }

  override fun createExpense(request: CreateExpenseRequest): Expense {
    if (ims.users.find { it.id == request.userId } == null) {
      ims.users[ims.users.indexOfFirst { it.id == request.userId }] =
        User(
          request.userId,
          ArrayList(ims.currencies),
          ims.users.find { it.id == request.userId }?.whatsNew ?: false
        )
    }
    val expense = Expense(
      ims.expenseCounter.incrementAndGet(),
      request.userId,
      request.groupId,
      request.telegramMessageId,
      request.amount,
      ims.users.find { it.id == request.userId }?.currencies?.get(0)?.currencyCode ?: "USD",
      Expense.Category.OTHER,
      Instant.now()
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
        .filter {
          it.date.isBefore(
            ZonedDateTime.now()
              .withDayOfMonth(1)
              .withHour(0)
              .withMinute(0)
              .withSecond(0)
              .toInstant()
          )
        })
    }
  }
}
