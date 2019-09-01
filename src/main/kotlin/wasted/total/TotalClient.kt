package wasted.total

import wasted.total.Total.Type

interface TotalClient {

  fun total(groupId: Long, type: Type, userId: Int): List<Total>
  fun totals(type: Type, userId: Int): List<Total>
}
