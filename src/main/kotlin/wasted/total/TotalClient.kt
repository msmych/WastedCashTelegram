package wasted.total

import wasted.total.Total.Type

interface TotalClient {

    fun getTotal(groupId: Long, type: Type): List<Total>
}