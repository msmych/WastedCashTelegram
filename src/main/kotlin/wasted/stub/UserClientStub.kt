package wasted.stub

import com.google.inject.Inject
import com.google.inject.Singleton
import wasted.user.User
import wasted.user.UserClient
import java.util.*
import kotlin.collections.ArrayList

@Singleton
class UserClientStub : UserClient {

  @Inject
  lateinit var ims: InMemoryStorage

  override fun existsUser(userId: Int): Boolean {
    return ims.users.any { it.id == userId }
  }

  override fun createUser(userId: Int) {
    ims.users.add(User(userId, ArrayList(ims.currencies), false))
  }

  override fun userCurrencies(userId: Int): List<Currency> {
    return ims.users.find { it.id == userId }?.currencies ?: emptyList()
  }

  override fun userWhatsNew(userId: Int): Boolean {
    return ims.users.find { it.id == userId }?.whatsNew ?: false
  }

  override fun whatsNewSubscribedIds(userId: Int): List<Int> {
    return ims.users.filter { it.whatsNew }.map { it.id }
  }

  override fun toggleUserCurrency(userId: Int, currency: String): List<Currency> {
    val cur = Currency.getInstance(currency)
    if (ims.users.find { it.id == userId }?.currencies?.contains(cur) != true) {
      ims.users.find { it.id == userId }?.currencies?.add(cur)
    } else if (ims.users.find { it.id == userId }?.currencies?.size ?: 0 > 1) {
      ims.users.find { it.id == userId }?.currencies?.remove(cur)
    }
    return ims.users.find { it.id == userId }?.currencies ?: emptyList()
  }

  override fun toggleUserWhatsNew(userId: Int): Boolean {
    if (ims.users.none { it.id == userId })
      ims.users.add(User(userId, ArrayList(ims.currencies), false))
    val user = ims.users.find { it.id == userId } ?: return false
    ims.users[ims.users.indexOfFirst { it.id == userId }] = User(user.id, user.currencies, !user.whatsNew)
    return !user.whatsNew
  }
}
