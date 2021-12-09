package com.rittmann.common.validations

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.rittmann.common.R
import com.rittmann.common.extensions.StateField
import com.rittmann.common.extensions.enableState

/**
 * This class can be heavier than CadastralUpdateTextWatcher, because execute the validation in all types
 * but you can handle generic functions and simplify the implementation.
 * Case you need improve the performance, please, create a new class ou adjust this class with care to
 * not impact the places where this is used.
 * */
class FieldValidation(
    var button: View? = null,
    var afterCallback: ((ValidationState) -> Unit)? = null
) {

    var started = false
    val arrayList = arrayListOf<Fields>()

    var inputErrorCallback: ((Fields) -> Unit)? = ::setErrorState
    var inputSuccessCallback: ((Fields) -> Unit)? = ::setSuccessState
    var inputNothingCallback: ((Fields) -> Unit)? = ::nothing

    var buttonSuccessColor: Int = R.color.robbie_button_color_secondary

    var setButtonState: Boolean = true

    enum class ValidationState {
        ALL_VALID, PARTIAL_VALID, INVALID
    }

    private val watcher = object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun afterTextChanged(editable: Editable) {
            runValidations()
        }
    }

    /**
     * Add a new field to validation list [arrayList]
     * Add a TextWatcher to the EditText, this way the inputs will be observers
     * After finish to prepare this class, use the functions [start] to init
     * @param editText the EditText to valid
     * @param line the line from the EditText usually used to change the button state
     * @param textError the TextView/label with the error message from the input
     * @param afterChangeErrorVisibility if the EditText is invalid, the textError will be hide
     * using INVISIBLE or GONE (or VISIBLE if you wish)
     * @param validationType the validation type used to compare with some constants, I'll create
     * specific constants to this (NUMBER, STREET, NAME, EMAIL, ...)
     * @param validationCallback the validation function, here you'll put your validation field code
     * */
    fun add(
        editText: EditText?,
        textError: TextView? = null,
        afterChangeErrorVisibility: Int = View.INVISIBLE,
        validationType: Int = -1,
        callValidationCallback: Boolean = true,
        validationCallback: ((String) -> Boolean)? = null
    ) {
        editText?.also {
            editText.addTextChangedListener(watcher)

            arrayList.add(
                Fields(
                    editText,
                    textError,
                    afterChangeErrorVisibility,
                    validationType,
                    callValidationCallback,
                    validationCallback
                )
            )
        }
    }

    /**
     * Adjust this code, setText and setSelection
     * */
    fun updateFieldText(editText: EditText?, value: String) {
        if (editText == null) return

        for (field in arrayList) {
            if (field.editText == editText) {
                editText.apply {
                    removeTextChangedListener(watcher)
                    setText(value)
//                    setSelection(value.length)
                    addTextChangedListener(watcher)
                }
                break
            }
        }
    }

    private fun validateFieldBy(field: Fields) {
//        val s = field.editText.text
//
//        when (field.validationType) {
//
//        }
    }

    fun start() {
        started = true
    }

    fun runValidations() {
        if (started.not()) return

        var isToCallValidationCallback = true
        var isValid = ValidationState.ALL_VALID

        for (field in arrayList) {
            if (field.callValidationCallback.not()) isToCallValidationCallback = false

            if (inputNothingCallback != null && field.editText.text.isEmpty()) {
                isValid = ValidationState.PARTIAL_VALID
                inputNothingCallback?.invoke(field)
            } else {
                if (field.validationType > 0) {
                    validateFieldBy(field)
                } else {
                    if (field.callback == null) continue

                    if (field.callback.invoke(field.editText.text.toString())) {
                        inputSuccessCallback?.invoke(field)
                    } else {
                        isValid = ValidationState.INVALID
                        inputErrorCallback?.invoke(field)

                        // why am I calling this function here?
                        afterCallback?.invoke(ValidationState.INVALID)
                    }
                }
            }
        }

        if (isToCallValidationCallback)
            afterCallback?.invoke(isValid)
    }

    private fun setSuccessState(fields: Fields) {
        fields.apply {
            textError?.visibility = View.GONE
            if (setButtonState)
                enableButton()
        }
    }

    private fun setErrorState(fields: Fields) {
        fields.apply {
            textError?.visibility = View.VISIBLE
            if (setButtonState)
                disableButton()
        }
    }

    fun enableButton() {
        button?.apply {
            if (this is Button) enableState(StateField.VALID)
            else enableState(StateField.VALID)
        }
    }

    fun disableButton() {
        button?.apply {
            if (this is Button) enableState(StateField.INVALID)
            else enableState(StateField.INVALID)
        }
    }

    fun forceError(disableButton: Boolean = false, index: Int = -1) {
        if (index == -1) {
            for (field in arrayList) {
                setErrorState(field)
            }
        } else {
            setErrorState(arrayList[index])
        }

        if (disableButton) disableButton()
    }

    private fun nothing(fields: Fields) {
        fields.apply {
            textError?.visibility = fields.afterChangeErrorVisibility
            disableButton()
        }
    }

    class Fields(
        val editText: EditText,
        val textError: TextView?,
        val afterChangeErrorVisibility: Int = View.INVISIBLE,
        val validationType: Int = -1,
        val callValidationCallback: Boolean = true,
        val callback: ((String) -> Boolean)? = null
    )
}
