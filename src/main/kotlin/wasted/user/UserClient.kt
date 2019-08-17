package wasted.user

import java.util.*

interface UserClient {

    fun existsUser(userId: Int): Boolean
    fun createUser(userId: Int)
    fun userCurrencies(userId: Int): List<Currency>
    fun whatsNewSubscribedIds(): List<Int>
    fun toggleUserCurrency(userId: Int, currency: String): List<Currency>
}
