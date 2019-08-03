package wasted.total.report

import wasted.keypad.TotalKeypad
import wasted.total.Total.Type.MONTH
import wasted.total.TotalClient
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MonthlyTotalReporter : Runnable {

    @Inject
    lateinit var totalClient: TotalClient
    @Inject
    lateinit var totalKeypad: TotalKeypad

    override fun run() {
        totalClient.totals(MONTH)
            .groupBy { it.groupId }
            .forEach { totalKeypad.send(it.key, it.value, MONTH) }
    }
}