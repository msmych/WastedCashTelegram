package wasted.stub

import com.google.inject.Inject
import com.google.inject.Singleton
import wasted.user.UserClient
import java.util.*
import kotlin.collections.ArrayList

@Singleton
class UserClientStub : UserClient {

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
}