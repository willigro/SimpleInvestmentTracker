package com.rittmann.common.extensions

import android.text.Spanned
import androidx.core.text.HtmlCompat
import com.rittmann.common.utils.EditDecimalFormatController
import com.rittmann.common.utils.FormatUtil
import java.math.BigDecimal
import java.util.concurrent.TimeUnit

fun String.isInt(): Boolean {
    return try {
        toInt()
        true
    } catch (e: Exception) {
        false
    }
}

fun String.fromHtml(): Spanned {
    return HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_LEGACY)
}

fun String?.toIntOrNull(): Int? {
    try {
        if (isNullOrEmpty()) return null
        return this!!.toInt()
    } catch (e: java.lang.Exception) {
        return null
    }
}

fun String?.toIntOrZero(): Int {
    try {
        if (isNullOrEmpty()) return 0
        return this!!.toInt()
    } catch (e: java.lang.Exception) {
        return 0
    }
}

fun String?.toDoubleOrZero(): Double {
    try {
        if (isNullOrEmpty()) return 0.0
        return this!!.toDouble()
    } catch (e: java.lang.Exception) {
        return 0.0
    }
}

fun Int?.toStringOrEmpty(): String {
    try {
        if (this == null) return ""
        val s = this.toString()
        if (s.isEmpty()) return ""
        return s
    } catch (e: java.lang.Exception) {
        return ""
    }
}

fun String.timeLabel(timeUnit: TimeUnit): String {
    return when (timeUnit) {
        TimeUnit.HOURS -> {
            if (this.toInt() > 1)
                "horas"
            else
                "hora"
        }
        TimeUnit.MINUTES -> {
            if (this.toInt() > 1)
                "minutos"
            else
                "minuto"
        }
        else -> ""
    }
}

fun String.toDoubleValid(): Double {
    if (this.isEmpty()) {
        return 0.0
    }

    return try {
        toDouble()
    } catch (e: Exception) {
        0.0
    }
}

fun String.toDoubleValid(places: Int): Double {
    if (this.isEmpty()) {
        return 0.0
    }

    return try {
        BigDecimal(this).setScale(places).toPlainString().toDouble()
    } catch (e: Exception) {
        0.0
    }
}

fun String.clearCurrency(): String {
    var r = replace(FormatUtil.CURRENCY_SYMBOL_DEFAULT_COIN, "")
        .replace(FormatUtil.CURRENCY_SYMBOL_REAL, "")

    if (contains("E").not())
        r = r.replace(".", "")

    return r.replace(",", "").trim()
}

fun String.clearDecimal(): String {
    var r = this
    if (contains("E").not())
        r = r.replace(".", "")

    return r.replace(",", "").trim()
}

fun String.containsCount(c: Char): Int {
    var count = 0
    this.forEach {
        if (it == c) count++
    }
    return count
}

fun String.getScale(): Int {
    return when {
        this.contains(",") -> {
            this.split(",").last().length
        }
        this.contains(".") -> {
            this.split(".").last().length
        }
        else -> EditDecimalFormatController.DEFAULT_SCALE
    }
}