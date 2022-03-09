package com.rittmann.common.customs

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import com.rittmann.common.R

@BindingAdapter("valueText")
fun CustomLabelValueText.setValueText(value: String?) {
    setValue(value.orEmpty())
    if (value != tag) { // Store the original value
        tag = value   // To prevent duplicate/extra modification
        setValue(value.orEmpty())
    }
}

@InverseBindingAdapter(attribute = "valueText")
fun CustomLabelValueText.getValueText(): String {
    return getValue()
}

class CustomLabelValueText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var label = ""
    private var value = ""

    private var valueTextView: TextView

    init {
        isClickable = true
        isFocusable = false

//        setBackgroundColor(ContextCompat.getColor(context, R.color.robbie_input_text_background))

        context.withStyledAttributes(attrs, R.styleable.CustomLabelValueText) {
            label = getString(R.styleable.CustomLabelValueText_labelValue_label).toString()
            value = getString(R.styleable.CustomLabelValueText_valueText).toString()
        }

        val customLayout =
            LayoutInflater.from(context).inflate(R.layout.custom_label_value, this, false)
        addView(customLayout)

        valueTextView = findViewById(R.id.value_label_custom_value_id)
        valueTextView.text = value

        findViewById<TextView>(R.id.value_label_custom_label_id).text = label
    }

    fun getValue() = valueTextView.text.toString()

    fun setValue(value: String) {
        valueTextView.text = value
    }
}