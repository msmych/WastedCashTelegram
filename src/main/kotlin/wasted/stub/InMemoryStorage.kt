package wasted.stub

import com.google.inject.Singleton
import wasted.expense.Expense
import wasted.group.Group
import wasted.keypad.CurrenciesKeypad.Companion.AVAILABLE_CURRENCIES
import wasted.user.User
import java.util.concurrent.atomic.AtomicLong

@Singleton
class InMemoryStorage {

  val expenseCounter = AtomicLong()

  val expenses = ArrayList<Expense>()
  val users = ArrayList<User>()
  val groups = ArrayList<Group>()
  val currencies = AVAILABLE_CURRENCIES
}
