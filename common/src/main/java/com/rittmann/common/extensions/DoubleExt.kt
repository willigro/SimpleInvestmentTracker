package com.rittmann.common.extensions

fun Double.getScale(): Int {
    return toString().getScale()
}

fun Double?.orZero(): Double {
    return this ?: 0.0
}