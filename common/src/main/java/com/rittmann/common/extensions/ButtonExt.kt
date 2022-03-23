package com.rittmann.common.extensions

import android.widget.Button
import com.rittmann.common.R

fun Button?.enableState(stateField: StateField) {
    this?.apply {
        isEnabled = stateField == StateField.VALID
        when (stateField) {
            StateField.INVALID -> setBackgroundResource(R.drawable.background_disabled_button_primary)
            else -> setBackgroundResource(R.drawable.background_enabled_button_primary)
        }
    }
}