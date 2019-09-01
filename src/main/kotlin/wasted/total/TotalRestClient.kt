package wasted.total

import com.google.gson.reflect.TypeToken
import wasted.rest.RestClient
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TotalRestClient : TotalClient {

  @Inject
  lateinit var restClient: RestClient

  override fun total(groupId: Long, type: Total.Type, userId: Int): List<Total> {
    return restClient.getForObject(
      "/total/in/$groupId/type/$type",
      userId,
      object : TypeToken<List<Total>>() {}.type
    )
  }

  override fun totals(type: Total.Type, userId: Int): List<Total> {
    return restClient.getForObject(
      "/total/type/$type",
      userId,
      object : TypeToken<List<Total>>() {}.type
    )
  }
}
