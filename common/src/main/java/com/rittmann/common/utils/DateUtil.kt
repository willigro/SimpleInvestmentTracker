package com.rittmann.common.utils

import java.util.*
import android.annotation.SuppressLint
import android.util.Log
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList

interface IDate {
    fun getCurrentDate(): Calendar
}

object DateUtil : IDate {

     const val REPRESENTATIVE_FORMAT = "dd/MM/yyyy"
    const val DB_FORMAT = "yyyy-MM-dd HH:mm:ss"
    const val monthCompletFormat = "MMMM"  // MMMM = June
    const val monthSimplifiedFormat = "MMM" // MMM = Jun
    const val monthNumericFormat = "MM" // MM = 6
    private val months: ArrayList<String> = ArrayList()
    private val days: ArrayList<String> = ArrayList()
    private val years: ArrayList<String> = ArrayList()

    fun getDays(): ArrayList<String> {
        if (days.size == 0) {
            var day = 1
            while (day < 32) {
                day++
                days.add(day.toString())
            }
        }
        return days
    }

    fun getYears(): ArrayList<String> {
        if (years.size == 0) {
            var year = 2040
            while (year > 1980) {
                year--
                years.add(year.toString())
            }
        }
        return years
    }

    fun dateTimeFormatoPTBR(calendar: Calendar): String {
        val dateFormat = DateFormat.getDateTimeInstance()
        dateFormat.timeZone = calendar.timeZone
        return dateFormat.format(calendar.time)
    }

    @SuppressLint("SimpleDateFormat")
    fun simpleDateFormat(calendar: Calendar, format: String = REPRESENTATIVE_FORMAT): String {
        val sFormat = SimpleDateFormat(format)
        sFormat.timeZone = calendar.timeZone
        return sFormat.format(calendar.time)
    }

    @SuppressLint("SimpleDateFormat")
    fun getMonthName(calendar: Calendar, monthFormat: String = monthCompletFormat, withYear: Boolean = true): String {
        val year = if (withYear) "/yyyy" else ""
        return SimpleDateFormat("$monthFormat$year").format(calendar.time)
    }

    fun getMonthSQLite(calendar: Calendar): String {
        val mont = calendar.get(Calendar.MONTH) + 1
        return if (mont < 10) {
            "0$mont"
        } else mont.toString()
    }

    fun getDaySQLite(calendar: Calendar): String {
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return if (day < 10) {
            "0$day"
        } else day.toString()
    }

    @SuppressLint("SimpleDateFormat")
    fun parseDate(string: String, format: String = REPRESENTATIVE_FORMAT): Calendar {
        val calendar = Calendar.getInstance()
        try {
            calendar.time = SimpleDateFormat(format).parse(string)!!
        } catch (e: ParseException) {
            e.printStackTrace()
            Log.i("teste", e.message.orEmpty())
        }

        return calendar
    }

    fun parseDate(day: Int, month: Int, year: Int): Calendar {
        val calendar = Calendar.getInstance()

        try {
            calendar.set(year, month - 1, day)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return calendar
    }

    fun getInitAndFinishWeek(calendar: Calendar): Pair<Calendar, Calendar> {
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        val calendarInit = calendar.clone() as Calendar

        calendarInit.let {
            it.add(Calendar.DAY_OF_MONTH, -(dayOfWeek - 1)) // - 1 is actual day
            it.get(Calendar.DAY_OF_MONTH)
        }

        calendar.let {
            calendar.add(Calendar.DAY_OF_MONTH, 7 - dayOfWeek)
            it.get(Calendar.DAY_OF_MONTH)
        }

        return Pair(calendarInit, calendar)
    }

    fun getInitAndFinishWeekFormated(calendar: Calendar): String {
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        val calendarInit = calendar.clone() as Calendar

        val dayToInit = calendarInit.let {
            it.add(Calendar.DAY_OF_MONTH, -(dayOfWeek - 1)) // - 1 is actual day
            it.get(Calendar.DAY_OF_MONTH)
        }

        val dayToEnd = calendar.let {
            calendar.add(Calendar.DAY_OF_MONTH, 7 - dayOfWeek)
            it.get(Calendar.DAY_OF_MONTH)
        }

        val labelInit = if (dayToInit < 10) "0$dayToInit" else dayToInit.toString()
        val labelEnd = if (dayToEnd < 10) "0$dayToEnd" else dayToEnd.toString()
        return "$labelInit (${getMonthName(calendarInit, monthSimplifiedFormat, false)}) - $labelEnd (${getMonthName(calendar, monthSimplifiedFormat, false)})"
    }

    @SuppressLint("SimpleDateFormat")
    private fun getFormat(): SimpleDateFormat {
        return SimpleDateFormat(REPRESENTATIVE_FORMAT)
    }

    override fun getCurrentDate(): Calendar {
        return Calendar.getInstance()
    }
}