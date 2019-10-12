package wasted.group

interface GroupClient {

  fun groupMonthlyReport(groupId: Long, userId: Int): Boolean
  fun monthlyReportGroupsIds(userId: Int): List<Long>
}
