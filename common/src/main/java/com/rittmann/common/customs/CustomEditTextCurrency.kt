package com.rittmann.common.customs

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
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
import com.rittmann.common.extensions.toIntOrZero
import com.rittmann.common.utils.EditCurrency
import com.rittmann.common.utils.EditCurrency.Companion.DEFAULT_SCALE
import com.rittmann.common.utils.EditCurrency.Companion.SCALE_LIMIT


@BindingAdapter("textFromEdit")
fun CustomEditTextCurrency.textFromEdit(value: String?) {
    if (editText?.tag == null) {
        editCurrency?.setScaleIfIsDifferent(value.orEmpty())?.also {
            textViewScale?.text = it.toString()
        }
        editText?.tag = 1
    }
    editCurrency?.setCurrency(value)
}

@InverseBindingAdapter(attribute = "textFromEdit")
fun CustomEditTextCurrency.textFromEdit(): String {
    return this.editCurrency?.normalCurrency().toString()
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
    editCurrency?.setScale(value)
}

@BindingAdapter(value = ["textFromEditAttrChanged"])
fun CustomEditTextCurrency.setListener(textAttrChanged: InverseBindingListener?) {
    if (textAttrChanged != null) {
        editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (this@setListener.editText?.tag != editable) {
                    textAttrChanged.onChange()
                }
            }
        })
    }
}

class CustomEditTextCurrency @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    var editCurrency: EditCurrency? = null
    var textView: TextView? = null
    var editText: EditText? = null
    var textViewScale: TextView? = null
    private var textViewScaleLabel: TextView? = null
    private var viewAddScale: TextView? = null
    private var viewRemoveScale: TextView? = null

    private var label = ""
    private var inputTypeEdit = ""
    private var inputValueScale = DEFAULT_SCALE

    init {
        isClickable = true
        isFocusable = false

        setBackgroundColor(ContextCompat.getColor(context, R.color.robbie_input_text_background))

        context.withStyledAttributes(attrs, R.styleable.CustomEditText) {
            label = getString(R.styleable.CustomEditText_labelText).toString()
            inputTypeEdit = getString(R.styleable.CustomEditText_inputTypeEdit).toString()
        }

        orientation = VERTICAL

        addLabel()

        addCurrencyInput()

        addScaleViews()

        val params =
            MarginLayoutParams(MarginLayoutParams.MATCH_PARENT, MarginLayoutParams.WRAP_CONTENT)
        setPadding(0, 22, 0, 0)
        layoutParams = params
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
                    count--
                    if (editCurrency?.setScale(count) == true)
                        textViewScale?.text = count.toString()
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
                    count++
                    if (editCurrency?.setScale(count) == true)
                        textViewScale?.text = count.toString()
                }
            }
        }
        linear.addView(viewAddScale)
    }

    private fun addCurrencyInput() {
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

            editCurrency = EditCurrency(this, inputValueScale)
        }

        addView(editText)
    }

    override fun performClick(): Boolean {
        if (super.performClick()) return true

        editText?.requestFocus()

        return true
    }

    fun setTextEditText(txt: String?) {
        editText?.setText(txt)
        val index = txt?.length ?: 0
        editText?.setSelection(index)
    }
}