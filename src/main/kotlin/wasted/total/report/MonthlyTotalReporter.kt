package wasted.total.report

import wasted.group.GroupClient
import wasted.keypad.TotalKeypad
import wasted.total.Total.Type.MONTH
import wasted.total.TotalClient
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MonthlyTotalReporter : Runnable {

  @Inject
  lateinit var userId: String
  @Inject
  lateinit var groupClient: GroupClient
  @Inject
  lateinit var totalClient: TotalClient
  @Inject
  lateinit var totalKeypad: TotalKeypad

  override fun run() {
    groupClient.monthlyReportGroupsIds(userId.toInt())
      .map { totalClient.total(it, MONTH, userId.toInt()) }
      .forEach { totalKeypad.send(it.first().groupId, it) }
  }
}
