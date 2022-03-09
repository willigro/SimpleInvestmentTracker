package com.rittmann.common.extensions

import android.widget.Button
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.rittmann.common.R

fun Button?.enableState(stateField: StateField) {
    this?.apply {
        isEnabled = stateField == StateField.VALID
        when (stateField) {
            StateField.INVALID -> setBackgroundResource(R.drawable.disabled_button)
            else -> setBackgroundResource(R.drawable.enabled_button)
        }
    }
}