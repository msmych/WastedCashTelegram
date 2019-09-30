package wasted.bot.update.processor

import org.telegram.telegrambots.meta.ApiContext
import wasted.bot.HelpUpdateProcessor
import wasted.bot.StartUpdateProcessor
import wasted.expense.*
import wasted.expense.clear.ClearByTypeUpdateProcessor
import wasted.expense.clear.ClearUpdateProcessor
import wasted.settings.SettingsUpdateProcessor
import wasted.settings.ToggleMonthlyReportUpdateProcessor
import wasted.settings.ToggleWhatsNewUpdateProcessor
import wasted.total.TotalMonthUpdateProcessor
import wasted.total.TotalUpdateProcessor
import wasted.user.ConfirmCurrenciesUpdateProcessor
import wasted.user.CurrenciesUpdateProcessor
import wasted.user.ToggleCurrencyUpdateProcessor

val updateProcessors = setOf(
  StartUpdateProcessor::class.java,
  CurrenciesUpdateProcessor::class.java,
  ToggleCurrencyUpdateProcessor::class.java,
  ConfirmCurrenciesUpdateProcessor::class.java,
  WastedUpdateProcessor::class.java,
  AmountUpdateProcessor::class.java,
  ExpenseUpdateProcessor::class.java,
  EditAmountUpdateProcessor::class.java,
  HelpUpdateProcessor::class.java,
  NextCurrencyUpdateProcessor::class.java,
  NextCurrencyOptionsUpdateProcessor::class.java,
  RemoveExpenseUpdateProcessor::class.java,
  CancelExpenseRemovalUpdateProcessor::class.java,
  ConfirmExpenseRemovalUpdateProcessor::class.java,
  EditCategoryUpdateProcessor::class.java,
  CategoryUpdateProcessor::class.java,
  OptionsUpdateProcessor::class.java,
  ClearUpdateProcessor::class.java,
  ClearByTypeUpdateProcessor::class.java,
  TotalMonthUpdateProcessor::class.java,
  TotalUpdateProcessor::class.java,
  SettingsUpdateProcessor::class.java,
  ToggleWhatsNewUpdateProcessor::class.java,
  ToggleMonthlyReportUpdateProcessor::class.java)
  .map { ApiContext.getInstance(it) }
