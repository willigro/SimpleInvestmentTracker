package com.rittmann.common.extensions

fun <T> List<T>.indexOfBy(predicate: (T) -> Boolean): Int {
    var index = -1
    var count = 0
    for (item in this) {
        if (predicate(item)) {
            index = count
            break
        }
        count++
    }
    return index
}