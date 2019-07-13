package wasted.keypad

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup

internal class CategoriesKeypadTest {

    private val bot = mock<TelegramLongPollingBot>()
    private val categoriesKeypad = CategoriesKeypad()

    @BeforeEach
    fun setUp() {
        categoriesKeypad.bot = bot
    }

    @Test
    fun updating() {
        categoriesKeypad.update(1, 2)
        verify(bot).execute(any<EditMessageReplyMarkup>())
    }
}