package com.rittmann.common.utils

import com.rittmann.common.datasource.basic.CurrencyType
import com.rittmann.common.extensions.clearCurrency
import com.rittmann.common.extensions.clearDecimal
import com.rittmann.common.extensions.toDoubleValid
import com.rittmann.common.utils.FormatUtil.CURRENCY_SYMBOL_DEFAULT_COIN
import com.rittmann.common.utils.FormatUtil.CURRENCY_SYMBOL_REAL
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.Locale

//
//object FormatUtil {
//    fun applyCurrency(
//        value: Double,
//        format: DecimalFormat
//    ): String =
//        NumberFormat.getCurrencyInstance(Locale("pt", "BR")).apply {
//            maximumFractionDigits = format.maximumFractionDigits
//        }.format(value)
//}
//
//class FormatCurrency {
//    var currencyFormatted: String = "0"
//    var normalCurrency: Double = 0.0
//    var decimal: Int = 0
//    var times: Int = 5
//
//    fun format(newCurrency: String): String {
////        val currency = when {
////            newCurrency.contains(",") -> {
////                if (newCurrency.split(",")[1].length < times)
////                    newCurrency + "0".repeat(times - newCurrency.split(",")[1].length)
////                else newCurrency
////            }
////            newCurrency.contains(".") -> {
////                if (newCurrency.split(".")[1].length < times)
////                    newCurrency + "0".repeat(times - newCurrency.split(".")[1].length)
////                else newCurrency
////            }
////            else -> newCurrency
////        }
//
////        val count = when {
////            newCurrency.contains(",") -> {
////                newCurrency.split(",")[1].length
////            }
////            newCurrency.containsCount('.', 1) -> {
////                // 100.000.000 -> do nothing
////                // 100.0 -> [0.]001
////                newCurrency.split(".")[1].length
////            }
////            else -> 1
////        }
////
////        val times = "0".repeat(count)
////        decimal = "1$times".toInt()
//
//        val format = DecimalFormat("#0.${if (times == 0) "##" else "#".repeat(times)}")
////        val format = DecimalFormat("#0.${if (times == "0") "##" else "#".repeat(count)}")
//
//        val cleanString = newCurrency.clearCurrency()
//        val parsed = cleanString.toDoubleValid()
//        normalCurrency = parsed // / decimal
//        val formatted = FormatUtil.applyCurrency(normalCurrency, format)
//
//        currencyFormatted = formatted
//        return currencyFormatted
//    }
//
//    fun normalize(cost: Double): Double {
//        return cost * decimal
//    }
//
//    fun isDifferent(currency: String): Boolean {
//        return currency.clearCurrency().toDoubleValid() !=
//                currencyFormatted.clearCurrency().toDoubleValid()
//    }
//}


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
}

interface FormatDecimal {
    fun format(newCurrency: String, scale: Int, decimal: Int): String
    fun isDifferent(currency: String): Boolean
    fun retrieveValue(): BigDecimal
    fun hasFormat(): Boolean
}

class FormatCurrency(var currencyType: CurrencyType) : FormatDecimal {
    var currencyFormatted: String = ""
    var normalCurrency: BigDecimal = BigDecimal("0.0")

    override fun format(newCurrency: String, scale: Int, decimal: Int): String {

        val cleanString = newCurrency.clearCurrency()

        val parsed = BigDecimal(cleanString).setScale(scale)

        normalCurrency = parsed.div(BigDecimal(decimal).setScale(scale))

        val formatted =
            if (currencyType == CurrencyType.REAL) {
                FormatUtil.applyCurrency(normalCurrency.toDouble(), scale)
            } else {
                FormatUtil.applyCurrency(
                    normalCurrency.toDouble(),
                    scale,
                    CURRENCY_SYMBOL_DEFAULT_COIN
                )
            }

        currencyFormatted = formatted.let {
            val diff = it.split(",")[1].length
            if (diff < scale)
                it + "0".repeat(scale - diff)
            else
                it
        }

        return currencyFormatted
    }

    override fun isDifferent(currency: String): Boolean {
        if (currencyFormatted.isEmpty()) return true

        return currency.clearCurrency().toDoubleValid() !=
                currencyFormatted.clearCurrency().toDoubleValid()
    }

    override fun retrieveValue(): BigDecimal = normalCurrency

    override fun hasFormat(): Boolean {
        return currencyFormatted.contains(CURRENCY_SYMBOL_REAL) ||
                currencyFormatted.contains(CURRENCY_SYMBOL_DEFAULT_COIN)
    }
}

class FormatNormalDecimal : FormatDecimal {
    var currencyFormatted: String = "0"
    var normalCurrency: BigDecimal = BigDecimal("0.0")

    override fun format(newCurrency: String, scale: Int, decimal: Int): String {

        val cleanString = newCurrency.clearDecimal()

        val parsed = BigDecimal(cleanString).setScale(scale)

        normalCurrency = parsed.div(BigDecimal(decimal).setScale(scale))

        val formatted = normalCurrency.toPlainString().let {
            val diff = it.split(".")[1].length
            if (diff < scale)
                it + "0".repeat(scale - diff)
            else
                it
        }

        currencyFormatted = formatted

        return currencyFormatted
    }

    override fun isDifferent(currency: String): Boolean {
        return currency.clearCurrency().toDoubleValid() !=
                currencyFormatted.clearCurrency().toDoubleValid()
    }

    override fun retrieveValue(): BigDecimal = normalCurrency

    override fun hasFormat(): Boolean {
        return currencyFormatted.isNotEmpty()
    }
}