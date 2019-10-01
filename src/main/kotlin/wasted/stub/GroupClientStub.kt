package wasted.stub

import wasted.group.GroupClient
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroupClientStub : GroupClient {

  @Inject
  lateinit var ims: InMemoryStorage

  override fun monthlyReportGroupsIds(): List<Long> =
    ims.groups.filter { it.monthlyReport }.map { it.id }
}
