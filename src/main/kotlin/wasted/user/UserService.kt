package wasted.user

import org.jvnet.hk2.annotations.Service
import wasted.rest.RestClient
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

@Service
class UserService {

    private val currenciesCache = ConcurrentHashMap<Int, List<Currency>>()

    @Inject
    lateinit var restClient: RestClient

    fun getCurrencies(fromId: Int): List<Currency> {
        if (currenciesCache.containsKey(fromId))
            return currenciesCache[fromId]?.toList() ?: emptyList()
        val currencies = restClient.getUserCurrencies(fromId)
        currenciesCache[fromId] = currencies
        return currencies.toList()
    }
}