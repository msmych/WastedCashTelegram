package wasted.user

import java.util.*

interface UserClient {

  fun existsUser(userId: Int): Boolean
  fun createUser(userId: Int)
  fun userCurrencies(userId: Int): List<Currency>
  fun userMonthlyReport(userId: Int): Boolean
  fun userWhatsNew(userId: Int): Boolean
  fun monthlyReportSubscribedIds(userId: Int): List<Int>
  fun whatsNewSubscribedIds(userId: Int): List<Int>
  fun toggleUserCurrency(userId: Int, currency: String): List<Currency>
  fun toggleUserMonthlyReport(userId: Int): Boolean
  fun toggleUserWhatsNew(userId: Int): Boolean
}
