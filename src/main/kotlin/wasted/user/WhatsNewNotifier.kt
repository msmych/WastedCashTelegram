package wasted.user

import org.telegram.telegrambots.bots.TelegramLongPollingBot
import wasted.ManifestUtils.manifestValue
import javax.inject.Inject

class WhatsNewNotifier {

  @Inject
  lateinit var userClient: UserClient
  @Inject
  lateinit var bot: TelegramLongPollingBot

  fun send() {
    WhatsNewNotifier::class.java.classLoader
      .getResource("messages/whats_new/${manifestValue("App-Version")}.md")
      .readText()
  }
}
