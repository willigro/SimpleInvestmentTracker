package com.rittmann.common.customs

import android.content.Context
import android.text.InputType
import android.text.method.DigitsKeyListener
import android.util.AttributeSet
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.rittmann.common.R
import com.rittmann.common.datasource.basic.CurrencyType
import com.rittmann.common.extensions.toDoubleValid
import com.rittmann.common.extensions.toIntOrZero
import com.rittmann.common.utils.EditDecimalFormatController
import com.rittmann.common.utils.EditDecimalFormatController.Companion.DEFAULT_SCALE
import com.rittmann.common.utils.EditDecimalFormatController.Companion.SCALE_LIMIT
import com.rittmann.common.utils.FormatCurrency
import com.rittmann.common.utils.FormatNormalDecimal
import com.rittmann.common.utils.FormatUtil


@BindingAdapter("textFromEdit")
fun CustomEditTextCurrency.textFromEdit(value: Double) {
    setTextEditText(value)
}

@InverseBindingAdapter(attribute = "textFromEdit")
fun CustomEditTextCurrency.textFromEdit(): Double {
    return this.editDecimalFormatController?.normalCurrency().toDoubleValid()
}

@BindingAdapter(value = ["textFromEditAttrChanged"])
fun CustomEditTextCurrency.setListener(textAttrChanged: InverseBindingListener?) {
    if (textAttrChanged != null) {
        editDecimalFormatController?.onChangeValue?.observeForever {
            textAttrChanged.onChange()
        }
    }
}

@BindingAdapter("currencyType")
fun CustomEditTextCurrency.currencyType(value: CurrencyType) {
    editDecimalFormatController?.setCurrencyType(value)
}

@InverseBindingAdapter(attribute = "currencyType")
fun CustomEditTextCurrency.currencyType(): CurrencyType {
    return editDecimalFormatController?.getCurrencyType() ?: CurrencyType.REAL
}

@BindingAdapter(value = ["currencyTypeAttrChanged"])
fun CustomEditTextCurrency.setListenerCurrencyTypeAttrChanged(textAttrChanged: InverseBindingListener?) {
    if (textAttrChanged != null) {
        editDecimalFormatController?.onChangeCurrencyType?.observeForever {
            textAttrChanged.onChange()
        }
    }
}

@BindingAdapter("labelText")
fun CustomEditTextCurrency.labelText(value: String?) {
    this.textView?.text = value
    if (value != tag) { // Store the original value
        tag = value   // To prevent duplicate/extra modification
        this.textView?.text = value
    }
}

@BindingAdapter("valueScale")
fun CustomEditTextCurrency.valueScale(value: Int) {
    editDecimalFormatController?.changeScale(value)
}

class CustomEditTextCurrency @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    var editDecimalFormatController: EditDecimalFormatController? = null
    var textView: TextView? = null
    var editText: EditText? = null
    var textViewScale: TextView? = null
    private var textViewScaleLabel: TextView? = null
    private var viewAddScale: TextView? = null
    private var viewRemoveScale: TextView? = null
    private var textViewReal: TextView? = null
    private var textViewCrypto: TextView? = null

    private var label = ""
    private var inputTypeEdit = ""
    private var formatType = ""
    private var inputValueScale = DEFAULT_SCALE

    init {
        isClickable = true
        isFocusable = false

        setBackgroundColor(ContextCompat.getColor(context, R.color.robbie_input_text_background))

        context.withStyledAttributes(attrs, R.styleable.CustomEditText) {
            label = getString(R.styleable.CustomEditText_labelText).toString()
            inputTypeEdit = getString(R.styleable.CustomEditText_inputTypeEdit).toString()
            formatType = getString(R.styleable.CustomEditText_formatType).toString()
        }

        orientation = VERTICAL

        addLabel()

        addInput()

        addScaleViews()

        addChangeCurrencyType()

        val params =
            MarginLayoutParams(MarginLayoutParams.MATCH_PARENT, MarginLayoutParams.WRAP_CONTENT)
        setPadding(0, 22, 0, 0)
        layoutParams = params
    }

    private fun addChangeCurrencyType() {
        if (formatType == DECIMAL) return

        val linear = LinearLayout(context).apply {
            orientation = HORIZONTAL

            val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            setPadding(10, 10, 10, 10)
            layoutParams = params
        }

        addView(linear)

        textViewReal = TextView(context).apply {
            val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            setPadding(10, 10, 10, 10)
            layoutParams = params
            text = FormatUtil.CURRENCY_SYMBOL_REAL

            setOnClickListener {
                editDecimalFormatController?.setCurrencyType(CurrencyType.REAL)
            }
        }

        linear.addView(textViewReal)

        textViewCrypto = TextView(context).apply {
            val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            setPadding(10, 10, 10, 10)
            layoutParams = params
            text = FormatUtil.CURRENCY_SYMBOL_DEFAULT_COIN

            setOnClickListener {
                editDecimalFormatController?.setCurrencyType(CurrencyType.CRYPTO)
            }
        }

        linear.addView(textViewCrypto)
    }

    private fun addLabel() {
        textView = TextView(context).apply {
            text = label
        }
        addView(textView)
    }

    private fun addScaleViews() {
        val linear = LinearLayout(context).apply {
            orientation = HORIZONTAL

            val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            setPadding(10, 10, 10, 10)
            layoutParams = params
        }

        addView(linear)

        textViewScaleLabel = TextView(context).apply {
            val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            setPadding(10, 10, 10, 10)
            layoutParams = params
            text = context.getString(R.string.custom_edit_currency_decimal_place_label)
        }
        linear.addView(textViewScaleLabel)

        viewRemoveScale = TextView(context).apply {
            val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            setPadding(10, 10, 10, 10)
            layoutParams = params
            text = "-"

            setOnClickListener {
                var count = textViewScale?.text.toString().toIntOrZero()
                if (count > 1) {
                    editDecimalFormatController?.changeScale(--count)
                }
            }
        }
        linear.addView(viewRemoveScale)

        textViewScale =
            TextView(context).apply {
                val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
                setPadding(10, 10, 10, 10)
                layoutParams = params
                isClickable = true

                text = "1"

                id = R.id.edit_custom_currency_scale_id
            }

        linear.addView(textViewScale)

        viewAddScale = TextView(context).apply {
            val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            setPadding(10, 10, 10, 10)
            layoutParams = params
            text = "+"

            setOnClickListener {
                var count = textViewScale?.text.toString().toIntOrZero()
                if (count < SCALE_LIMIT) {
                    editDecimalFormatController?.changeScale(++count)
                }
            }
        }
        linear.addView(viewAddScale)

        editDecimalFormatController?.onChangeScale?.observeForever {
            textViewScale?.text = it.toString()
        }
    }

    private fun addInput() {
        editText = EditText(ContextThemeWrapper(context, R.style.Robbie_TextInput), null, 0).apply {
            val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            params.topMargin = 8
            layoutParams = params
            isClickable = true
            isFocusable = true
            setTextIsSelectable(true)
            setRawInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL)
            inputType =
                InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_NUMBER_FLAG_SIGNED
            keyListener = DigitsKeyListener.getInstance("0123456789")

            id = R.id.edit_custom_id

            val formatter =
                if (formatType == DECIMAL) FormatNormalDecimal() else FormatCurrency(CurrencyType.REAL)

            editDecimalFormatController =
                EditDecimalFormatController(this, inputValueScale, formatter)
        }

        addView(editText)
    }

    override fun performClick(): Boolean {
        if (super.performClick()) return true

        editText?.requestFocus()

        return true
    }

    fun setTextEditText(value: Double) {
        if (editText?.tag == null) {
            editDecimalFormatController?.setScaleIfIsDifferent(value)?.also {
                textViewScale?.text = it.toString()
            }
            editText?.tag = 1
        }
        editDecimalFormatController?.setCurrency(value)
    }

    companion object {
        const val DECIMAL = "decimal"
    }
}