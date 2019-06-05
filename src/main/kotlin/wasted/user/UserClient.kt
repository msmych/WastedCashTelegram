package wasted.user

import java.util.*

interface UserClient {

    fun existsUser(userId: Int): Boolean
    fun createUser(userId: Int)
    fun getUserCurrencies(userId: Int): List<Currency>
    fun toggleUserCurrency(userId: Int, currency: String): List<Currency>
}