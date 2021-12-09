package com.rittmann.common.customs

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.withStyledAttributes
import com.rittmann.common.R

class CustomSpinner @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var textView: TextView
    private var line: View
    private var label = ""
    var spinner: Spinner

    init {
        isClickable = false
        isFocusable = false

        context.withStyledAttributes(attrs, R.styleable.CustomEditText) {
            label = getString(R.styleable.CustomEditText_labelText).toString()
        }

        orientation = VERTICAL

        textView = TextView(ContextThemeWrapper(context, R.style.TextView), null, 0).apply {
            text = label
        }
        addView(textView)

        spinner = Spinner(ContextThemeWrapper(context, R.style.Spinner)).apply {
            val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            params.topMargin = 8
            layoutParams = params
            isClickable = true
            isFocusable = true

            id = R.id.spinner_custom_id
        }
        addView(spinner)

        line = LinearLayout(ContextThemeWrapper(context, R.style.Divisor), null, 0).apply {
            layoutParams = LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    1
            ).also {
                it.topMargin = 6
            }
        }
        addView(line)

        val params = MarginLayoutParams(MarginLayoutParams.MATCH_PARENT, MarginLayoutParams.WRAP_CONTENT)
        setPadding(0, 22, 0, 0)
        layoutParams = params
    }
}