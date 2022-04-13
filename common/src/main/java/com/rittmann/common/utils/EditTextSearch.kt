package com.rittmann.common.utils

import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import androidx.core.widget.doAfterTextChanged
import java.util.*
import kotlin.concurrent.schedule

class EditTextSearch(
    private val editText: EditText,
    clearImage: ImageView? = null,
    val delay: Long = DELAY,
    val callback: (String) -> Unit
) {
    private var watcher: TextWatcher
    private var timer: Timer? = null

    var onStart: (() -> Unit)? = null

    init {
        watcher = editText.doAfterTextChanged { value ->
            timer = newTimer()
            timer?.schedule(delay) {
                callback(value.toString())
            }
        }
        clearImage?.setOnClickListener {
            timer?.cancel()
            editText.removeTextChangedListener(watcher)
            editText.setText("")
            callback("")
            editText.addTextChangedListener(watcher)
        }
    }

    private fun newTimer(): Timer {
        timer?.cancel()
        return Timer(NAME, false).also {
            onStart?.invoke()
        }
    }

    fun changeValue(value: String?) {
        timer?.cancel()
        editText.apply {
            removeTextChangedListener(watcher)
            setText(value)
            setSelection(value?.length ?: 0)
            addTextChangedListener(watcher)
        }
    }

    companion object {
        const val NAME = "EditTextSearch timer"
        const val DELAY = 350L
        const val DELAY_TEST = 450L
    }
}