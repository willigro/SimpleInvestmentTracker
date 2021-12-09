package com.rittmann.common.extensions

import android.widget.Button
import androidx.core.content.ContextCompat
import com.rittmann.common.R

fun Button?.enableState(stateField: StateField) {
    this?.apply {
        isEnabled = stateField == StateField.VALID
        background = when (stateField) {
            StateField.VALID -> ContextCompat.getDrawable(context, R.drawable.robbie_button_background_primary)
            else -> ContextCompat.getDrawable(context, R.drawable.robbie_button_background_drawable_secondary)
        }
    }
}