package com.rittmann.common.customs

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import androidx.constraintlayout.widget.ConstraintSet
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import com.rittmann.common.R

@BindingAdapter("valueText")
fun CustomLabelValueText.setValueText(value: String?) {
    this.textViewValue.text = value.orEmpty()
    if (value != tag) { // Store the original value
        tag = value   // To prevent duplicate/extra modification
        this.textViewValue.text = value.orEmpty()
    }
}

@InverseBindingAdapter(attribute = "valueText")
fun CustomLabelValueText.getValueText(): String {
    return this.textViewValue.text.toString()
}

class CustomLabelValueText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var textViewLabel: TextView
    var textViewValue: TextView

    private var label = ""
    private var value = ""

    init {
        isClickable = true
        isFocusable = false

        setBackgroundColor(ContextCompat.getColor(context, R.color.robbie_input_text_background))

        context.withStyledAttributes(attrs, R.styleable.CustomLabelValueText) {
            label = getString(R.styleable.CustomLabelValueText_labelValue_label).toString()
            value = getString(R.styleable.CustomLabelValueText_valueText).toString()
        }

        textViewLabel = TextView(context).apply {
            text = label
            id = R.id.value_label_custom_label_id
            addView(this)

            val constraintSet = ConstraintSet()
            constraintSet.clone(this@CustomLabelValueText)
            constraintSet.connect(
                this.id,
                ConstraintSet.START,
                this@CustomLabelValueText.id,
                ConstraintSet.START,
                0
            )
            constraintSet.connect(
                this.id,
                ConstraintSet.TOP,
                this@CustomLabelValueText.id,
                ConstraintSet.TOP,
                0
            )
            constraintSet.applyTo(this@CustomLabelValueText)
        }

        textViewValue = TextView(context).apply {
            text = value
            id = R.id.value_label_custom_value_id
            addView(this)

            val constraintSet = ConstraintSet()
            constraintSet.clone(this@CustomLabelValueText)
            constraintSet.connect(
                this@apply.id,
                ConstraintSet.END,
                this@CustomLabelValueText.id,
                ConstraintSet.END,
                0
            )
            constraintSet.connect(
                this@apply.id,
                ConstraintSet.TOP,
                this@CustomLabelValueText.id,
                ConstraintSet.TOP,
                0
            )
            constraintSet.applyTo(this@CustomLabelValueText)
        }

        val params =
            MarginLayoutParams(MarginLayoutParams.MATCH_PARENT, MarginLayoutParams.WRAP_CONTENT)
        setPadding(0, 22, 0, 0)
        layoutParams = params
    }
}