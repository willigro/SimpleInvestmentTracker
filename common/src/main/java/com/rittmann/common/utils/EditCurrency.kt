package com.rittmann.common.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.text.DecimalFormat

class EditCurrency(private val editText: EditText, scale: Int) {

    private val formatCurrency = FormatCurrency(scale)
    private var watcher: TextWatcher? = null

    init {
        watcher = object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                s?.toString()?.also { currency ->
                    if (formatCurrency.isDifferent(currency)) {
                        formatTheInput(currency)
                    }
                }
            }
        }

        editText.addTextChangedListener(watcher)
        editText.setText(formatCurrency.format("0"))
    }

    private fun formatTheInput(currency: String) {
        watcher?.apply {
            editText.removeTextChangedListener(this)

            formatCurrency.format(currency).also { formatted ->
                editText.setText(formatted)
                editText.setSelection(formatted.length)
                editText.addTextChangedListener(this)
            }
        }
    }

//    fun getValue(): Double {
//        return formatCurrency.normalCurrency
//    }

    fun setCurrency(currency: Double) {
        formatCurrency.normalize(currency).also { normalized ->
            try {
                val df = DecimalFormat("#")
                df.maximumFractionDigits = 2
                editText.setText(df.format(normalized))
            } catch (e: Exception) {
                editText.setText(normalized.toString())
            }
        }
    }

    fun setCurrency(currency: String?) {
        editText.setText(currency)
    }

    fun normalCurrency() = formatCurrency.normalCurrency

    fun setScale(scale: Int): Boolean {
        return if (formatCurrency.setScale(scale)) {
            formatTheInput(editText.text.toString())
            true
        } else
            false
    }

    fun setScaleIfIsDifferent(currency: String) = formatCurrency.setScaleIfIsDifferent(currency)
}