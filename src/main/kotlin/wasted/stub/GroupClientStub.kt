package wasted.stub

import wasted.group.GroupClient
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroupClientStub : GroupClient {

  @Inject
  lateinit var ims: InMemoryStorage

  override fun groupMonthlyReport(groupId: Long, userId: Int): Boolean =
    ims.groups.find { it.id == groupId }?.monthlyReport ?: false

  override fun monthlyReportGroupsIds(userId: Int): List<Long> =
    ims.groups.filter { it.monthlyReport }.map { it.id }
}
