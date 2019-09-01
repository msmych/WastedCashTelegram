package wasted.stub

import com.google.inject.Inject
import com.google.inject.Singleton
import wasted.expense.Expense
import wasted.total.Total
import wasted.total.Total.Type
import wasted.total.Total.Type.ALL
import wasted.total.Total.Type.MONTH
import wasted.total.TotalClient
import java.time.Instant
import java.time.ZonedDateTime.now

@Singleton
class TotalClientStub : TotalClient {

    @Inject
    lateinit var ims: InMemoryStorage

    override fun total(groupId: Long, type: Type, userId: Int): List<Total> {
        if (type == ALL)
            return toTotalList(ims.expenses.filter { it.groupId == groupId })
        return toTotalList(ims.expenses
            .filter { it.groupId == groupId }
            .filter { it.date.isAfter(date(type)) })
    }

    private fun toTotalList(expenses: List<Expense>): List<Total> {
        return expenses.groupBy { it.currency }
            .map { cur ->
                cur.value.groupBy { it.category }
                    .map { cat -> cat.value
                        .map {
                            Total(it.groupId, it.userId, it.amount, cur.key, cat.key)
                        }.reduce { acc, total ->
                            Total(total.groupId, total.userId, acc.amount + total.amount, total.currency, total.category)
                        }
                    }
            }.flatten()
    }

    private fun date(type: Type): Instant? {
        return (
            when (type) {
                MONTH -> now().withDayOfMonth(1)
                else -> throw IllegalArgumentException()
            })
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .toInstant()
    }

    override fun totals(type: Type, userId: Int): List<Total> {
        if (type == ALL) {
            return toTotalList(ims.expenses)
        }
        return toTotalList(ims.expenses.filter { it.date.isAfter(date(type)) })
    }
}
