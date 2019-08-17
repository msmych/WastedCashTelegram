package wasted.user

import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.ParseMode.MARKDOWN
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import wasted.ManifestUtils.manifestValue
import javax.inject.Inject

class WhatsNewNotifier {

  @Inject
  lateinit var userClient: UserClient
  @Inject
  lateinit var bot: TelegramLongPollingBot

  fun send() {
    val text = WhatsNewNotifier::class.java.classLoader
      .getResource("messages/whats-new/${manifestValue("App-Version")}.md")
      ?.readText() ?: ""
    userClient.whatsNewSubscribedIds()
      .map {
        SendMessage(it.toLong(), text)
          .setParseMode(MARKDOWN)
      }.forEach { bot.execute(it) }
  }
}
