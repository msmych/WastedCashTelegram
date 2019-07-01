package wasted.total

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.inject.Singleton
import org.apache.http.client.fluent.Request.Get

@Singleton
class TotalRestClient : TotalClient {

    private val gson = Gson()

    private val baseUrl = "http://localhost:8080"

    lateinit var apiToken: String

    override fun getTotal(groupId: Long, type: Total.Type): List<Total> {
        return gson.fromJson(Get("$baseUrl/total/in/$groupId/type/$type")
            .addHeader("api-token", apiToken)
            .execute().returnContent().asString(),
            object: TypeToken<List<Total>>(){}.type)
    }
}