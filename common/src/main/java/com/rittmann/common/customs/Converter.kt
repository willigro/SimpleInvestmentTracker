package com.rittmann.common.customs

import androidx.databinding.InverseMethod
import com.rittmann.common.extensions.getScale
import com.rittmann.common.extensions.toDoubleOrZero
import com.rittmann.common.extensions.toIntOrZero
import com.rittmann.common.utils.Constants.CRYPTO_SIGN
import com.rittmann.common.utils.Constants.REAL_SIGN
import java.math.BigDecimal

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

    @InverseMethod("bigDecimalToDouble")
    @JvmStatic
    fun doubleToBigDecimal(value: Double): BigDecimal {
        return BigDecimal(value).setScale(value.getScale(), BigDecimal.ROUND_CEILING)
    }

    @JvmStatic
    fun bigDecimalToDouble(value: BigDecimal): Double {
        return value.toDouble()
    }
}