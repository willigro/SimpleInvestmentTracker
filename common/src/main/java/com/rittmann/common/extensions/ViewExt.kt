package com.rittmann.common.extensions

import android.view.View
import androidx.core.content.ContextCompat
import com.rittmann.androidtools.log.log
import com.rittmann.common.R
import kotlin.math.roundToInt

fun View?.visible() {
    this?.visibility = View.VISIBLE
}

fun View?.invisible() {
    this?.visibility = View.INVISIBLE
}

fun View?.gone() {
    this?.visibility = View.GONE
}

fun View?.isGone(): Boolean {
    return this?.visibility == View.GONE
}

enum class StateField {
    DEFAULT, INVALID, VALID
}

fun View?.enableState(stateField: StateField) {
    this?.apply {
        background = when (stateField) {
            StateField.DEFAULT -> ContextCompat.getDrawable(
                context,
                R.drawable.border_outline_input
            )
            StateField.INVALID -> ContextCompat.getDrawable(
                context,
                R.drawable.border_outline_input_error
            )
            StateField.VALID -> ContextCompat.getDrawable(
                context,
                R.drawable.border_outline_input_valid
            )
        }
    }
}

fun Int.isResId() = this != -1

// Use it inside the post method
fun View?.heightPercentage(percentage: Int): Int {
    return (this?.height ?: 0) * percentage / 100
}
