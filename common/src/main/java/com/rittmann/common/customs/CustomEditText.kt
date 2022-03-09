package com.rittmann.common.customs

import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.view.ContextThemeWrapper
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.rittmann.common.R


@BindingAdapter("textFromEdit")
fun CustomEditText.textFromEdit(value: String?) {
    this.editText?.setText(value)
    if (value != tag) { // Store the original value
        tag = value   // To prevent duplicate/extra modification
        this.editText?.setText(value)
    }
}

@InverseBindingAdapter(attribute = "textFromEdit")
fun CustomEditText.textFromEdit(): String {
    return this.editText?.text?.toString() ?: ""
}

@BindingAdapter("labelText")
fun CustomEditText.labelText(value: String?) {
    this.textView.text = value
    if (value != tag) { // Store the original value
        tag = value   // To prevent duplicate/extra modification
        this.textView.text = value
    }
}

@BindingAdapter(value = ["textFromEditAttrChanged"])
fun CustomEditText.setListener(textAttrChanged: InverseBindingListener?) {
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

class CustomEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    var textView: TextView
    private var label = ""
    private var inputTypeEdit = ""

    var editText: EditText? = null

    init {
        isClickable = true
        isFocusable = false

//        setBackgroundColor(ContextCompat.getColor(context, R.color.robbie_input_text_background))

        context.withStyledAttributes(attrs, R.styleable.CustomEditText) {
            label = getString(R.styleable.CustomEditText_labelText).toString()
            inputTypeEdit = getString(R.styleable.CustomEditText_inputTypeEdit).toString()
        }

        val customLayout =
            LayoutInflater.from(context).inflate(R.layout.custom_edit_text_with_label, this, false)
        addView(customLayout)

        textView = findViewById<TextView>(R.id.txt_custom_edit_text_with_label).apply {
            text = label
        }

        editText = findViewById<EditText>(R.id.edit_custom_id).apply {
            setRawInputType(
                when (inputTypeEdit) {
                    "number" -> {
                        keyListener = DigitsKeyListener.getInstance("0123456789")
                        InputType.TYPE_CLASS_NUMBER
                    }
                    "textCapCharacters" -> {
                        filters = arrayOf<InputFilter>(InputFilter.AllCaps())

                        InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
                    }
                    else -> {
                        InputType.TYPE_CLASS_TEXT
                    }
                }
            )
        }


//        orientation = VERTICAL

//        textView = TextView(context).apply {
//            setTextAppearance(context, R.style.Robbie_Paragraph1)
//            text = label
//        }
//        addView(textView)
//
//        editText = EditText(ContextThemeWrapper(context, R.style.Robbie_TextInput), null, 0).apply {
//            val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
//            params.topMargin = 8
//            layoutParams = params
//            isClickable = true
//            isFocusable = true
//            setTextIsSelectable(true)
//            setRawInputType(InputType.TYPE_CLASS_TEXT)
//
//            setRawInputType(
//                when (inputTypeEdit) {
//                    "number" -> {
//                        keyListener = DigitsKeyListener.getInstance("0123456789")
//                        InputType.TYPE_CLASS_NUMBER
//                    }
//                    "textCapCharacters" -> {
//                        filters = arrayOf<InputFilter>(InputFilter.AllCaps())
//
//                        InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
//                    }
//                    else -> {
//                        InputType.TYPE_CLASS_TEXT
//                    }
//                }
//            )
//
//            id = R.id.edit_custom_id
//        }
//        addView(editText)

        val params =
            MarginLayoutParams(MarginLayoutParams.MATCH_PARENT, MarginLayoutParams.WRAP_CONTENT)
        setPadding(0, 22, 0, 0)
        layoutParams = params
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