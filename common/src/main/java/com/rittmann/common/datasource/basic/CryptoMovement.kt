package com.rittmann.common.datasource.basic

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rittmann.common.datasource.dao.config.TableCryptoMovement
import com.rittmann.common.utils.EditDecimalFormatController.Companion.DEFAULT_SCALE
import java.io.Serializable
import java.math.BigDecimal

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

    @ColumnInfo(name = TableCryptoMovement.OPERATED_AMOUNT)
    var operatedAmount: BigDecimal = BigDecimal(0.0).setScale(DEFAULT_SCALE),

    @ColumnInfo(name = TableCryptoMovement.CURRENT_VALUE)
    var currentValue: BigDecimal = BigDecimal(0.0).setScale(DEFAULT_SCALE),

    @ColumnInfo(name = TableCryptoMovement.CURRENT_VALUE_CURRENCY)
    var currentValueCurrency: CurrencyType = CurrencyType.REAL,

    @ColumnInfo(name = TableCryptoMovement.TOTAL_VALUE)
    var totalValue: BigDecimal = BigDecimal(0.0).setScale(DEFAULT_SCALE),

    @ColumnInfo(name = TableCryptoMovement.TOTAL_VALUE_CURRENCY)
    var totalValueCurrency: CurrencyType = CurrencyType.REAL,

    @ColumnInfo(name = TableCryptoMovement.TAX)
    var tax: BigDecimal = BigDecimal(0.0).setScale(DEFAULT_SCALE),

    @ColumnInfo(name = TableCryptoMovement.TAX_CURRENCY)
    var taxCurrency: CurrencyType = CurrencyType.CRYPTO,
) : Serializable, Ponjo() {

    override fun isInserting(): Boolean = id == 0L

    fun calculateTotalValue(): Double =
        if (totalValueCurrency == CurrencyType.REAL)
            totalValue.toDouble()
        else {
            operatedAmount.toDouble() * currentValue.toDouble()
        }

    fun calculateTaxValue(): Double =
        if (taxCurrency == CurrencyType.REAL) {
            tax.toDouble()
        } else
            tax.toDouble() * currentValue.toDouble()

}

enum class CryptoOperationType(val value: String) : Serializable {
    BUY("B"), SELL("S");

    companion object {
        fun convert(valueToConvert: String): CryptoOperationType {
            return if (valueToConvert == BUY.value) BUY
            else SELL
        }
    }
}

enum class CurrencyType(val value: String) : Serializable {
    REAL("r"), CRYPTO("c");

    companion object {
        fun convert(valueToConvert: String): CurrencyType {
            return if (valueToConvert == REAL.value) REAL
            else CRYPTO
        }
    }
}