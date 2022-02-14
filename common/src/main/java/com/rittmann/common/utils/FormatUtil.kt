package com.rittmann.common.utils

import com.rittmann.common.datasource.basic.CurrencyType
import com.rittmann.common.extensions.clearCurrency
import com.rittmann.common.utils.FormatUtil.CURRENCY_SYMBOL_DECIMAL
import com.rittmann.common.utils.FormatUtil.CURRENCY_SYMBOL_DEFAULT_COIN
import com.rittmann.common.utils.FormatUtil.CURRENCY_SYMBOL_REAL
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.*

object FormatUtil {
    fun applyCurrency(
        value: Double,
        scale: Int
    ): String =
        NumberFormat.getCurrencyInstance(Locale("pt", "BR")).apply {
            maximumFractionDigits = scale
        }.format(value)

    fun applyCurrency(
        value: Double,
        scale: Int,
        currencySymbol: String
    ): String {
        val df = NumberFormat.getCurrencyInstance()
        val dfs = DecimalFormatSymbols()
        dfs.currencySymbol = currencySymbol.let {
            if (it.contains(" ").not())
                "$it "
            else
                it
        }
        dfs.groupingSeparator = '.'
        dfs.monetaryDecimalSeparator = ','
        (df as DecimalFormat).decimalFormatSymbols = dfs
        df.maximumFractionDigits = scale
        return df.format(value)
    }

    const val CURRENCY_SYMBOL_REAL = "R$"
    const val CURRENCY_SYMBOL_DEFAULT_COIN = "C"
    const val CURRENCY_SYMBOL_DECIMAL = ""
}

interface FormatDecimal {
    fun format(newCurrency: String, scale: Int, decimal: Double): String
    fun isDifferent(currency: String): Boolean
    fun retrieveValue(): BigDecimal
    fun hasFormat(): Boolean
}

class FormatCurrency(var currencyType: CurrencyType) : FormatDecimal {
    private var currencyFormatted: String = ""
    private var normalCurrency: BigDecimal = BigDecimal("0.0")

    override fun format(newCurrency: String, scale: Int, decimal: Double): String {

        val cleanString = newCurrency.clearCurrency()

        val parsed = BigDecimal(cleanString).setScale(scale)

        normalCurrency = parsed.div(BigDecimal(decimal).setScale(scale))

        val formatted =
            when (currencyType) {
                CurrencyType.REAL -> {
                    FormatUtil.applyCurrency(normalCurrency.toDouble(), scale)
                }
                CurrencyType.CRYPTO -> {
                    FormatUtil.applyCurrency(
                        normalCurrency.toDouble(),
                        scale,
                        CURRENCY_SYMBOL_DEFAULT_COIN
                    )
                }
                else -> {
                    FormatUtil.applyCurrency(
                        normalCurrency.toDouble(),
                        scale,
                        CURRENCY_SYMBOL_DECIMAL
                    )
                }
            }

        currencyFormatted = formatted.let {
            if (it.contains(",")) {
                val diff = it.split(",")[1].length
                if (diff < scale)
                    it + "0".repeat(scale - diff)
                else
                    it
            } else
                it
        }

        return currencyFormatted
    }

    override fun isDifferent(currency: String): Boolean {
        if (currencyFormatted.isEmpty()) return true

        return currency != currencyFormatted
    }

    override fun retrieveValue(): BigDecimal = normalCurrency

    override fun hasFormat(): Boolean {
        return currencyFormatted.contains(CURRENCY_SYMBOL_REAL) ||
                currencyFormatted.contains(CURRENCY_SYMBOL_DEFAULT_COIN)
    }
}