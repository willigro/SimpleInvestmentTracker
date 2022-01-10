package com.rittmann.common.extensions

import com.rittmann.common.utils.EditDecimalFormatController

fun Double.getScale(): Int {
    val str = this.toString()
    return when {
        str.contains(",") -> {
            str.split(",").last().length
        }
        str.contains(".") -> {
            str.split(".").last().length
        }
        else -> EditDecimalFormatController.DEFAULT_SCALE
    }
}