package com.rittmann.common.utils

import android.app.DatePickerDialog
import android.content.Context
import java.util.*

class DateListenerUtil {

    var calendar: Calendar = Calendar.getInstance()
    private var dateListener: DatePickerDialog.OnDateSetListener? = null

    fun configureListener(callback: (calendar: Calendar) -> Unit) {
        dateListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                callback(calendar)
            }
    }

    fun show(context: Context) {
        DatePickerDialog(
            context,
            dateListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
}