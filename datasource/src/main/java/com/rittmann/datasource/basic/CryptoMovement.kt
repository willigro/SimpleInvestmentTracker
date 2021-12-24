package com.rittmann.datasource.basic

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rittmann.datasource.dao.config.TableCryptoMovement

/**
 * I'm letting this stuff here cause I wanna see how use it from here
 * */
@Entity(tableName = TableCryptoMovement.TABLE)
data class CryptoMovement(
    @ColumnInfo(name = TableCryptoMovement.ID)
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    @ColumnInfo(name = TableCryptoMovement.DATE)
    var date: String = "",

    @ColumnInfo(name = TableCryptoMovement.NAME)
    var name: String = "",

    @ColumnInfo(name = TableCryptoMovement.TYPE)
    var type: CryptoOperationType = CryptoOperationType.BUY,

    @ColumnInfo(name = TableCryptoMovement.BOUGHT_AMOUNT)
    var boughtAmount: Double = 0.0,

    @ColumnInfo(name = TableCryptoMovement.CURRENT_VALUE)
    var currentValue: Double = 0.0,

    @ColumnInfo(name = TableCryptoMovement.CURRENT_VALUE_CURRENCY)
    var currentValueCurrency: CurrencyType = CurrencyType.REAL,

    @ColumnInfo(name = TableCryptoMovement.TOTAL_VALUE)
    var totalValue: Double = 0.0,

    @ColumnInfo(name = TableCryptoMovement.TOTAL_VALUE_CURRENCY)
    var totalValueCurrency: CurrencyType = CurrencyType.REAL,

    @ColumnInfo(name = TableCryptoMovement.TAX)
    var tax: Double = 0.0,

    @ColumnInfo(name = TableCryptoMovement.TAX_CURRENCY)
    var taxCurrency: CurrencyType = CurrencyType.REAL,
)

enum class CryptoOperationType(val value: String) {
    BUY("B"), SELL("S");

    companion object {
        fun convert(valueToConvert: String): CryptoOperationType {
            return if (valueToConvert == BUY.value) BUY
            else SELL
        }
    }
}

enum class CurrencyType(val value: String) {
    REAL("r"), CRYPTO("c");

    companion object {
        fun convert(valueToConvert: String): CurrencyType {
            return if (valueToConvert == REAL.value) REAL
            else CRYPTO
        }
    }
}