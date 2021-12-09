package com.rittmann.common.extensions

import android.text.Spanned
import androidx.core.text.HtmlCompat
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