package com.rittmann.common.customs

import androidx.databinding.InverseMethod
import com.rittmann.common.extensions.toDoubleOrZero
import com.rittmann.common.extensions.toIntOrZero

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
        return value.toDoubleOrZero()
    }

    @JvmStatic
    fun doubleToString(value: Double): String {
        return value.toString()
    }
}