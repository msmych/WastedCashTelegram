package wasted.total.report

import org.slf4j.LoggerFactory
import wasted.keypad.TotalKeypad
import wasted.total.TotalClient
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MonthlyTotalReporter : Runnable {

    private val log = LoggerFactory.getLogger(MonthlyTotalReporter::class.java)

    @Inject
    lateinit var totalClient: TotalClient
    @Inject
    lateinit var totalKeypad: TotalKeypad

    override fun run() {
        log.info(totalClient.toString())
    }
}