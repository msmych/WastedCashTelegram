package wasted.total

interface TotalClient {

    fun getTotal(groupId: Long): List<Total>
}