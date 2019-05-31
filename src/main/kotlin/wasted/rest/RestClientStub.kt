package wasted.rest

import com.google.inject.Inject
import com.google.inject.Singleton
import wasted.expense.Expense
import wasted.expense.Expense.Category.OTHER
import wasted.expense.clear.ClearExpenseType
import wasted.expense.clear.ClearExpenseType.ALL
import wasted.stub.InMemoryStorage
import java.util.*
import kotlin.collections.ArrayList

@Singleton
class RestClientStub : RestClient {

    @Inject
    lateinit var ims: InMemoryStorage

    override fun existsUser(userId: Int): Boolean {
        return ims.userCurrencies.containsKey(userId)
    }

    override fun createUser(userId: Int) {
        ims.userCurrencies[userId] = ArrayList(ims.currencies)
    }

    override fun getUserCurrencies(userId: Int): List<Currency> {
        return ims.userCurrencies[userId] ?: emptyList()
    }

    override fun toggleUserCurrency(userId: Int, currency: String): List<Currency> {
        if (ims.userCurrencies[userId]?.contains(Currency.getInstance(currency)) != true)
            ims.userCurrencies[userId]?.add(Currency.getInstance(currency))
        else if (ims.userCurrencies[userId]?.size ?: 0 > 1)
            ims.userCurrencies[userId]?.remove(Currency.getInstance(currency))
        return ims.userCurrencies[userId] ?: emptyList()
    }

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
            OTHER,
            Date())
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
        }
    }
}