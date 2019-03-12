package wasted.bot.update.processor

import org.telegram.telegrambots.meta.api.objects.Update

interface UpdateProcessor {

    fun appliesTo(update: Update): Boolean

    fun process(update: Update)
}