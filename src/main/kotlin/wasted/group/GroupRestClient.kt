package wasted.group

import wasted.rest.RestClient
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroupRestClient : GroupClient {

  @Inject
  lateinit var restClient: RestClient

  override fun groupMonthlyReport(groupId: Long, userId: Int): Boolean =
    restClient.getForString("/group/$groupId/monthly-report", userId).toBoolean()

  override fun monthlyReportGroupsIds(userId: Int): List<Long> =
    restClient.getForObject("/groups/monthly-report/ids", userId, Array<Long>::class.java).toList()
}
