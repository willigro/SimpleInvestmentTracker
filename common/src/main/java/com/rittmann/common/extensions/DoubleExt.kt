package com.rittmann.common.extensions

import java.math.BigDecimal

fun Double.getScale(): Int {
    return toString().getScale()
}

fun Double?.orZero(): Double {
    return this ?: 0.0
}

fun Double.toBigDecimal(): BigDecimal {
    return BigDecimal(this).setScale(getScale())
}