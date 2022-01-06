package com.rittmann.common.utils

import com.rittmann.common.extensions.clearCurrency
import com.rittmann.common.extensions.toDoubleValid
import java.math.BigDecimal
import java.text.DecimalFormat
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
        format: DecimalFormat
    ): String =
        NumberFormat.getCurrencyInstance(Locale("pt", "BR")).apply {
            maximumFractionDigits = format.maximumFractionDigits
        }.format(value)

    fun applyCurrency(
        value: Double,
        format: Int
    ): String =
        NumberFormat.getCurrencyInstance(Locale("pt", "BR")).apply {
            maximumFractionDigits = format
        }.format(value)
}

class FormatCurrency(
    var times: Int = DEFAULT_SCALE
) {
    var currencyFormatted: String = "0"
    var normalCurrency: BigDecimal = BigDecimal("0.0")
    var decimal: Int = "1${"0".repeat(times)}".toInt()

    fun format(newCurrency: String): String {

        val cleanString = newCurrency.clearCurrency()

        val parsed = BigDecimal(cleanString).setScale(times)

        normalCurrency = parsed.div(BigDecimal(decimal).setScale(times))

        val formatted = FormatUtil.applyCurrency(normalCurrency.toDouble(), times).let {
            val diff = it.split(",")[1].length
            if (diff < times)
                it + "0".repeat(times - diff)
            else
                it
        }

        currencyFormatted = formatted

        return currencyFormatted
    }

    fun normalize(cost: Double): Double {
        return cost * decimal
    }

    fun isDifferent(currency: String): Boolean {
        return currency.clearCurrency().toDoubleValid() !=
                currencyFormatted.clearCurrency().toDoubleValid()
    }

    fun setScale(scale: Int): Boolean {
        return if (scale < SCALE_LIMIT) {
            times = scale
            decimal = "1${"0".repeat(times)}".toInt()
            true
        } else
            false
    }

    fun setScaleIfIsDifferent(currency: String): Int {
        val scale = when {
            currency.contains(",") -> {
                currency.split(",")[1].length
            }
            currency.contains(".") -> {
                currency.split(".")[1].length
            }
            else -> times
        }

        if (scale != times)
            setScale(scale)

        return scale
    }

    companion object {
        const val DEFAULT_SCALE = 5
        const val SCALE_LIMIT = 10
    }
}