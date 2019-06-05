package wasted.stub

import com.google.inject.Inject
import com.google.inject.Singleton
import wasted.total.Total
import wasted.total.TotalClient

@Singleton
class TotalClientStub : TotalClient {

    @Inject
    lateinit var ims: InMemoryStorage

    override fun getTotal(groupId: Long): List<Total> {
        return ims.expenses
            .filter { it.groupId == groupId }
            .map { Total(it.userId, it.amount, it.currency, it.category) }
    }
}