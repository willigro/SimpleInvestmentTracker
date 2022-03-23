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

interface ListableString {
    fun label(): String
}

fun List<ListableString>.toStringList(): List<String> {
    val arr = arrayListOf<String>()
    forEach {
        arr.add(it.label())
    }
    return arr
}

inline fun <T> List<T>.containsElementThat(predicate: (T) -> Boolean): Boolean {
    for (e in this) {
        if (predicate(e)) return true
    }
    return false
}