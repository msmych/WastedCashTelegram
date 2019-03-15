package wasted.expense

import java.util.*

fun formatAmount(amount: Long, currency: Currency): String {
    val sb = StringBuilder(amount.toString())
    if (amount < 100)
        sb.insert(0, '0')
    if (amount < 10)
        sb.insert(0, '0')
    sb.insert(sb.length - 2, '.')
    return "`$sb ${currency.symbol}`"
}