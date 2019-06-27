package wasted.stub

import com.google.inject.Inject
import com.google.inject.Singleton
import wasted.total.Total
import wasted.total.TotalClient
import java.time.ZonedDateTime
import java.util.*

@Singleton
class TotalClientStub : TotalClient {

    @Inject
    lateinit var ims: InMemoryStorage

    override fun getTotal(groupId: Long): List<Total> {
        return ims.expenses
            .filter { it.groupId == groupId }
            .map { Total(it.userId, it.amount, it.currency, it.category) }
    }

    override fun getRecentTotal(groupId: Long, period: String): List<Total> {
        return ims.expenses
            .filter { it.groupId == groupId }
            .filter { it.date.after(Date.from(when (period) {
                "month" -> ZonedDateTime.now().withDayOfMonth(1)
                else -> throw IllegalArgumentException()
            }
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .toInstant()))
            }.map { Total(it.userId, it.amount, it.currency, it.category) }
    }
}