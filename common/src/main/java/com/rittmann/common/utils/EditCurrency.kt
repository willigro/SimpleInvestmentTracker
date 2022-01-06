package com.rittmann.common.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class EditCurrency(private val editText: EditText, var scale: Int = DEFAULT_SCALE) {

    private val formatCurrency = FormatCurrency()
    private var watcher: TextWatcher? = null
    var decimal: Int = 10

    init {
        setScaleAndDecimal(scale)
    }

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
        editText.setText(formatCurrency.format("0", scale, decimal))
    }

    private fun formatTheInput(currency: String) {
        watcher?.apply {
            editText.removeTextChangedListener(this)

            formatCurrency.format(currency, scale, decimal).also { formatted ->
                editText.setText(formatted)
                editText.setSelection(formatted.length)
                editText.addTextChangedListener(this)
            }
        }
    }

    fun setCurrency(currency: String?) {
        editText.setText(currency)
    }

    fun normalCurrency() = formatCurrency.normalCurrency

    fun setScale(scale: Int): Boolean {
        return if (setScaleAndDecimal(scale)) {
            formatTheInput(editText.text.toString())
            true
        } else
            false
    }

    fun setScaleAndDecimal(scale: Int): Boolean {
        return if (scale < SCALE_LIMIT) {
            this.scale = scale
            decimal = "1${"0".repeat(this.scale)}".toInt()
            true
        } else
            false
    }

    fun setScaleIfIsDifferent(currency: String): Int {
        val scale = when {
            currency.contains(",") -> {
                currency.split(",").last().length
            }
            currency.contains(".") -> {
                currency.split(".").last().length
            }
            else -> scale
        }

        if (scale != this.scale)
            setScale(scale)

        return scale
    }

    companion object {
        const val DEFAULT_SCALE = 5
        const val SCALE_LIMIT = 10
    }
}