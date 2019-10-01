package wasted.settings

import org.telegram.telegrambots.meta.api.objects.Update
import wasted.bot.update.processor.UpdateProcessor
import javax.inject.Singleton

@Singleton
class GroupSettingsUpdateProcessor : UpdateProcessor {
  override fun appliesTo(update: Update): Boolean {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun process(update: Update) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
}
