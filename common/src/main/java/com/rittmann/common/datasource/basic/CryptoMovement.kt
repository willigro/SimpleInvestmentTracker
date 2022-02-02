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

    @Transient
    var useThisDate: Boolean = false

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

    companion object {
        fun buy(
            name: String,
            date: String,
            operatedAmount: Double,
            currentValue: Double,
            totalValue: Double,
            tax: Double
        ) = CryptoMovement(
            name = name,
            date = date,
            type = CryptoOperationType.BUY,
            operatedAmount = operatedAmount.toBigDecimal(),
            currentValue = currentValue.toBigDecimal(),
            currentValueCurrency = CurrencyType.REAL,
            totalValue = totalValue.toBigDecimal(),
            totalValueCurrency = CurrencyType.REAL,
            tax = tax.toBigDecimal(),
            taxCurrency = CurrencyType.CRYPTO
        )

        fun sell(
            name: String,
            date: String,
            operatedAmount: Double,
            currentValue: Double,
            totalValue: Double,
            tax: Double
        ) = CryptoMovement(
            name = name,
            date = date,
            type = CryptoOperationType.SELL,
            operatedAmount = operatedAmount.toBigDecimal(),
            currentValue = currentValue.toBigDecimal(),
            currentValueCurrency = CurrencyType.REAL,
            totalValue = totalValue.toBigDecimal(),
            totalValueCurrency = CurrencyType.REAL,
            tax = tax.toBigDecimal(),
            taxCurrency = CurrencyType.REAL
        )
    }
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
    REAL("r"), CRYPTO("c"), DECIMAL("d");

    companion object {
        fun convert(valueToConvert: String): CurrencyType {
            return when (valueToConvert) {
                REAL.value -> REAL
                DECIMAL.value -> DECIMAL
                else -> CRYPTO
            }
        }
    }
}