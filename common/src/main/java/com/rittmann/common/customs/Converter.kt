package com.rittmann.common.customs

import androidx.databinding.InverseMethod
import com.rittmann.common.extensions.toDoubleOrZero
import com.rittmann.common.extensions.toIntOrZero
import com.rittmann.common.utils.Constants.CRYPTO_SIGN
import com.rittmann.common.utils.Constants.REAL_SIGN
import com.rittmann.datasource.basic.CurrencyType

object Converter {
    @InverseMethod("intToString")
    @JvmStatic
    fun stringToInt(value: String): Int {
        return value.toIntOrZero()
    }

    @JvmStatic
    fun intToString(value: Int): String {
        return value.toString()
    }

    @InverseMethod("doubleToString")
    @JvmStatic
    fun stringToDouble(value: String): Double {
        return value
            .replace(REAL_SIGN, "")
            .replace(CRYPTO_SIGN, "")
            .replace(" ", "")
            .toDoubleOrZero()
    }

    @JvmStatic
    fun doubleToString(value: Double): String {
        return value.toString()
    }

    @JvmStatic
    fun doubleToString(value: Double, currencyType: CurrencyType): String {
        return when (currencyType) {
            CurrencyType.REAL -> "$REAL_SIGN $value"
            CurrencyType.CRYPTO -> "$CRYPTO_SIGN $value"
        }
    }
}