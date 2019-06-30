package wasted.stub

import com.google.inject.Inject
import com.google.inject.Singleton
import wasted.expense.Expense
import wasted.total.Total
import wasted.total.TotalClient
import java.time.ZonedDateTime
import java.util.*

@Singleton
class TotalClientStub : TotalClient {

    @Inject
    lateinit var ims: InMemoryStorage

    override fun getTotal(groupId: Long): List<Total> {
        return toTotalList(ims.expenses.filter { it.groupId == groupId })
    }

    private fun toTotalList(expenses: List<Expense>): List<Total> {
        return expenses.groupBy { it.currency }
            .map { cur ->
                cur.value.groupBy { it.category }
                    .map { cat -> cat.value
                        .map {
                            Total(it.userId, it.amount, cur.key, cat.key)
                        }.reduce { acc, total ->
                            Total(total.userId, acc.amount + total.amount, total.currency, total.category)
                        }
                    }
            }.flatten()
    }

    override fun getRecentTotal(groupId: Long, period: String): List<Total> {
        return toTotalList(ims.expenses
            .filter { it.groupId == groupId }
            .filter { it.date.after(Date.from(when (period) {
                "month" -> ZonedDateTime.now().withDayOfMonth(1)
                else -> throw IllegalArgumentException()
            }
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .toInstant()))
            })
    }
}