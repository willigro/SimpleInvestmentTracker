package com.rittmann.common.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import com.rittmann.common.datasource.basic.CurrencyType
import java.math.BigDecimal

class EditDecimalFormatController(
    private val editText: EditText,
    var scale: Int = DEFAULT_SCALE,
    var formatDecimal: FormatDecimal = FormatCurrency(CurrencyType.REAL)
) {

    private var watcher: TextWatcher? = null
    var decimal: Int = 10

    var onChangeScale: MutableLiveData<Int> = MutableLiveData<Int>()
    var onChangeValue: MutableLiveData<BigDecimal> = MutableLiveData<BigDecimal>()

    init {
        setScaleAndDecimal(scale)
    }

    init {
        watcher = object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                s?.toString()?.also { currency ->
                    if (formatDecimal.isDifferent(currency)) {
                        formatTheInput(currency)
                    }
                }
            }
        }

        editText.addTextChangedListener(watcher)
        setCurrency("0")
    }

    private fun formatTheInput(currency: String) {
        watcher?.apply {
            editText.removeTextChangedListener(this)

            formatDecimal.format(currency, scale, decimal).also { formatted ->
                editText.setText(formatted)
                editText.setSelection(formatted.length)
                editText.addTextChangedListener(this)

                if (onChangeValue.hasActiveObservers())
                    onChangeValue.value = normalCurrency()
            }
        }
    }

    fun setCurrency(currency: String?) {
        editText.setText(currency)
    }

    fun setCurrency(currency: Double) {
        formatTheInput(
            BigDecimal(currency).setScale(scale, BigDecimal.ROUND_CEILING).toPlainString()
        )
    }

    fun normalCurrency() = formatDecimal.retrieveValue()

    fun changeScale(scale: Int) {
        if (setScaleAndDecimal(scale))
            formatTheInput(editText.text.toString())
    }

    private fun setScaleAndDecimal(scale: Int): Boolean {
        return if (scale < SCALE_LIMIT) {
            this.scale = scale
            // todo: Use Math.pow() instead of this repeat
            decimal = "1${"0".repeat(this.scale)}".toInt()

            onChangeScale.value = scale
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
            changeScale(scale)

        return scale
    }

    companion object {
        const val DEFAULT_SCALE = 5
        const val SCALE_LIMIT = 10
    }
}