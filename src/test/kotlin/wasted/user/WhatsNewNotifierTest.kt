package wasted.user

import com.nhaarman.mockitokotlin2.*
import org.junit.Before
import org.junit.Test
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

internal class WhatsNewNotifierTest {

  private val userClient = mock<UserClient>()
  private val bot = mock<TelegramLongPollingBot>()

  private val whatsNewNotifier = WhatsNewNotifier()

  private val ids = listOf(1, 2, 3)

  @Before
  fun setUp() {
    whatsNewNotifier.userClient = userClient
    whatsNewNotifier.bot = bot

    whenever(userClient.whatsNewSubscribedIds(1234)).thenReturn(ids)
  }

  @Test
  fun sending() {
    whatsNewNotifier.send(1234)
    verify(bot, times(ids.size)).execute(any<SendMessage>())
  }
}
