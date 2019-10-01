package wasted.group

data class Group(val id: Long,
                 val userIds: ArrayList<Long>,
                 val monthlyReport: Boolean = false)
