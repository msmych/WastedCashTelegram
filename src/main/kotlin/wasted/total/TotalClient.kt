package wasted.total

interface TotalClient {

    fun getTotal(groupId: Long): List<Total>
    fun getRecentTotal(groupId: Long, period: String): List<Total>
}