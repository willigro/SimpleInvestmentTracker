package com.rittmann.common.datasource.dao.config

import androidx.room.TypeConverter
import com.rittmann.common.datasource.basic.CryptoOperationType
import com.rittmann.common.datasource.basic.CurrencyType
import com.rittmann.common.extensions.getScale
import com.rittmann.common.utils.DateUtil
import java.math.BigDecimal
import java.util.*

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

    @TypeConverter
    fun stringCurrencyToBigDecimal(value: String): BigDecimal {
        return BigDecimal(value).setScale(value.getScale())
    }

    @TypeConverter
    fun bigDecimalToStringCurrency(value: BigDecimal): String {
        return value.toPlainString()
    }
}