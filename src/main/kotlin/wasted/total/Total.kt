package wasted.total

import wasted.expense.Expense.Category

data class Total(val groupId: Long,
                 val userId: Int,
                 val amount: Long,
                 val currency: String,
                 val category: Category) {

    enum class Type {
        MONTH,
        ALL
    }
}