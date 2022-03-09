package com.rittmann.common.customs

import android.content.Context
import android.text.InputType
import android.text.method.DigitsKeyListener
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.view.ContextThemeWrapper
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.rittmann.common.R
import com.rittmann.common.datasource.basic.CurrencyType
import com.rittmann.common.extensions.gone
import com.rittmann.common.extensions.toDoubleValid
import com.rittmann.common.extensions.toIntOrZero
import com.rittmann.common.extensions.visible
import com.rittmann.common.utils.EditDecimalFormatController
import com.rittmann.common.utils.EditDecimalFormatController.Companion.DEFAULT_SCALE
import com.rittmann.common.utils.EditDecimalFormatController.Companion.SCALE_LIMIT
import com.rittmann.common.utils.FormatCurrency
import com.rittmann.common.utils.FormatUtil
import java.math.BigDecimal


@BindingAdapter("textFromEdit")
fun CustomEditTextCurrency.textFromEdit(value: BigDecimal) {
    setTextEditText(value)
}

@InverseBindingAdapter(attribute = "textFromEdit")
fun CustomEditTextCurrency.textFromEdit(): BigDecimal {
    return this.editDecimalFormatController?.normalCurrency() ?: BigDecimal(0.0)
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
    var imageOpenOptions: ImageView? = null
    private var textViewScaleLabel: TextView? = null
    private var viewAddScale: TextView? = null
    private var viewRemoveScale: TextView? = null
    private var textViewReal: TextView? = null
    private var textViewCrypto: TextView? = null
    private var containerOptions: View? = null

    private var label = ""
    private var inputTypeEdit = ""
    private var formatType = ""
    private var inputValueScale = DEFAULT_SCALE
    private var enableChangeCurrencyType: Boolean = true

    var viewRoot: View

    init {
        isClickable = true
        isFocusable = false

//        setBackgroundColor(ContextCompat.getColor(context, R.color.robbie_input_text_background))

        context.withStyledAttributes(attrs, R.styleable.CustomEditText) {
            label = getString(R.styleable.CustomEditText_labelText).toString()
            inputTypeEdit = getString(R.styleable.CustomEditText_inputTypeEdit).toString()
            formatType = getString(R.styleable.CustomEditText_formatType).toString()
            enableChangeCurrencyType =
                getBoolean(R.styleable.CustomEditText_enableChangeCurrencyType, true)
        }

        viewRoot =
            LayoutInflater.from(context).inflate(R.layout.custom_edit_text_currency, this, false)
        addView(viewRoot)

        containerOptions = viewRoot.findViewById(R.id.container_options)

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
        if (formatType == DECIMAL || enableChangeCurrencyType.not()) {
            findViewById<View>(R.id.container_currency_type).gone()
        }

        textViewReal = findViewById<TextView>(R.id.txt_currency_real).apply {
            text = FormatUtil.CURRENCY_SYMBOL_REAL

            setOnClickListener {
                editDecimalFormatController?.setCurrencyType(CurrencyType.REAL)
            }
        }

        textViewCrypto = findViewById<TextView>(R.id.txt_currency_crypto).apply {
            text = FormatUtil.CURRENCY_SYMBOL_DEFAULT_COIN

            setOnClickListener {
                editDecimalFormatController?.setCurrencyType(CurrencyType.CRYPTO)
            }
        }
    }

    private fun addLabel() {
        textView = findViewById<TextView>(R.id.txt_custom_edit_currency_label).apply {
            text = label
        }
    }

    private fun addScaleViews() {
        textViewScaleLabel = findViewById<TextView>(R.id.txt_label_scale).apply {
            text = context.getString(R.string.custom_edit_currency_decimal_place_label)
        }

        viewRemoveScale = findViewById<TextView>(R.id.view_remove_scale).apply {
            text = "-"

            setOnClickListener {
                var count = textViewScale?.text.toString().toIntOrZero()
                if (count > 1) {
                    editDecimalFormatController?.changeScale(--count)
                }
            }
        }

        textViewScale = findViewById<TextView>(R.id.txt_scale).apply {
            isClickable = true

            text = "1"
        }

        viewAddScale = findViewById<TextView>(R.id.view_add_scale).apply {
            text = "+"

            setOnClickListener {
                var count = textViewScale?.text.toString().toIntOrZero()
                if (count < SCALE_LIMIT) {
                    editDecimalFormatController?.changeScale(++count)
                }
            }
        }

        editDecimalFormatController?.onChangeScale?.observeForever {
            textViewScale?.text = it.toString()
        }
    }

    private fun addInput() {
        imageOpenOptions = findViewById<ImageView>(R.id.image_view_open_custom_edit_options).apply {
            tag = true

            setOnClickListener {
                if (tag == true) {
                    setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            android.R.drawable.arrow_down_float
                        )
                    )
                    tag = false

                    containerOptions.visible()

                } else {
                    setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            android.R.drawable.arrow_up_float
                        )
                    )
                    tag = true

                    containerOptions.gone()
                }

                setColorFilter(ContextCompat.getColor(context, android.R.color.black))
            }
        }

        editText = findViewById<EditText>(R.id.edit_custom_id).apply {
            setRawInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL)
            inputType =
                InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_NUMBER_FLAG_SIGNED
            keyListener = DigitsKeyListener.getInstance("0123456789")

            val formatter =
                if (formatType == DECIMAL) FormatCurrency(CurrencyType.DECIMAL) else FormatCurrency(
                    CurrencyType.REAL
                )

            editDecimalFormatController =
                EditDecimalFormatController(this, inputValueScale, formatter)
        }
    }

    override fun performClick(): Boolean {
        if (super.performClick()) return true

        editText?.requestFocus()

        return true
    }

    fun setTextEditText(value: BigDecimal) {
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