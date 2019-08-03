package wasted.total

import wasted.total.Total.Type

interface TotalClient {

    fun total(groupId: Long, type: Type): List<Total>
    fun totals(type: Type): List<Total>
}