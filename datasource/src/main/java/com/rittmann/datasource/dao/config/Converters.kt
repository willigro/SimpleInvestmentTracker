package com.rittmann.datasource.dao.config

import androidx.room.TypeConverter
import com.rittmann.common.utils.DateUtil
import com.rittmann.datasource.basic.CryptoOperationType
import com.rittmann.datasource.basic.CurrencyType
import java.util.Calendar

class Converters {
    @TypeConverter
    fun fromTimestamp(value: String?): Calendar? {
        if (value == null) return null
        return DateUtil.parseDate(value, DateUtil.DB_FORMAT)
    }

    @TypeConverter
    fun dateToTimestamp(date: Calendar?): String? {
        if (date == null) return null
        return DateUtil.simpleDateFormat(date, DateUtil.DB_FORMAT)
    }

    @TypeConverter
    fun convertStringToCryptoOperationType(value: String): CryptoOperationType {
        return CryptoOperationType.convert(value)
    }

    @TypeConverter
    fun convertCryptoOperationTypeToString(date: CryptoOperationType): String {
        return date.value
    }

    @TypeConverter
    fun convertStringToCurrencyType(value: String): CurrencyType {
        return CurrencyType.convert(value)
    }

    @TypeConverter
    fun convertCurrencyTypeToString(date: CurrencyType): String {
        return date.value
    }
}