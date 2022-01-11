package com.rittmann.common.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import com.rittmann.common.datasource.basic.CurrencyType
import com.rittmann.common.extensions.getScale
import java.math.BigDecimal
import kotlin.math.pow

class EditDecimalFormatController(
    private val editText: EditText,
    var scale: Int = DEFAULT_SCALE,
    var formatDecimal: FormatDecimal = FormatCurrency(CurrencyType.REAL)
) {

    private var watcher: TextWatcher? = null
    var decimal: Double = 10.0

    var onChangeScale: MutableLiveData<Int> = MutableLiveData<Int>()
    var onChangeValue: MutableLiveData<BigDecimal> = MutableLiveData<BigDecimal>()
    var onChangeCurrencyType: MutableLiveData<CurrencyType> = MutableLiveData<CurrencyType>()

    init {
        setScaleAndDecimal(scale)

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
        setCurrency(0.0)
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

    fun setCurrency(currency: Double) {
        formatTheInput(
            BigDecimal(currency).setScale(scale, BigDecimal.ROUND_CEILING).toPlainString()
        )
    }

    fun setCurrency(currency: BigDecimal) {
        formatTheInput(
            currency.setScale(scale, BigDecimal.ROUND_CEILING).toPlainString()
        )
    }

    fun normalCurrency() = formatDecimal.retrieveValue()

    fun changeScale(scale: Int) {
        if (setScaleAndDecimal(scale))
            formatTheInput(editText.text.toString())
    }

    private fun setScaleAndDecimal(scale: Int): Boolean {
        return if (scale <= SCALE_LIMIT) {
            this.scale = scale
            decimal = 10.0.pow(scale)

            onChangeScale.value = scale
            true
        } else
            false
    }

    fun setScaleIfIsDifferent(currency: BigDecimal): Int {
        val scale = currency.toPlainString().getScale()

        if (scale != this.scale)
            changeScale(scale)

        return scale
    }

    fun setCurrencyType(currencyType: CurrencyType) {
        if (formatDecimal is FormatCurrency) {
            (formatDecimal as FormatCurrency).currencyType = currencyType

            onChangeCurrencyType.value = currencyType

            formatTheInput(editText.text.toString())
        }
    }

    fun getCurrencyType(): CurrencyType {
        return if (formatDecimal is FormatCurrency) {
            (formatDecimal as FormatCurrency).currencyType
        } else
            CurrencyType.REAL
    }

    companion object {
        const val DEFAULT_SCALE = 5
        const val SCALE_LIMIT = 9
    }
}

object FormatDecimalController {

    fun format(
        value: Double,
        currencyType: CurrencyType? = null,
        valueScale: Int? = null
    ): String {
        val scale = valueScale ?: value.getScale()
        val decimal = 10.0.pow(scale.toDouble())

        return if (currencyType == null)
            FormatNormalDecimal().format(
                BigDecimal(value).setScale(
                    scale,
                    BigDecimal.ROUND_CEILING
                ).toPlainString(), scale, decimal
            )
        else
            FormatCurrency(currencyType).format(
                BigDecimal(value).setScale(
                    scale,
                    BigDecimal.ROUND_CEILING
                ).toPlainString(), scale, decimal
            )
    }

    fun format(
        value: BigDecimal,
        currencyType: CurrencyType? = null
    ): String {
        val scale = value.scale()
        val decimal = 10.0.pow(scale.toDouble())

        return if (currencyType == null)
            FormatNormalDecimal().format(
                value.toPlainString(), scale, decimal
            )
        else
            FormatCurrency(currencyType).format(
                value.toPlainString(), scale, decimal
            )
    }

    fun format(
        value: String,
        currencyType: CurrencyType? = null
    ): String {
        val scale = value.getScale()
        val decimal = 10.0.pow(scale.toDouble())

        return if (currencyType == null)
            FormatNormalDecimal().format(
                BigDecimal(value).setScale(
                    scale,
                    BigDecimal.ROUND_CEILING
                ).toPlainString(), scale, decimal
            )
        else
            FormatCurrency(currencyType).format(
                BigDecimal(value).setScale(
                    scale,
                    BigDecimal.ROUND_CEILING
                ).toPlainString(), scale, decimal
            )
    }
}