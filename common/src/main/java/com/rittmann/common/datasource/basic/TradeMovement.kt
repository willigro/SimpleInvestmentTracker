package com.rittmann.common.datasource.basic

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rittmann.common.datasource.dao.config.TableTradeMovement
import com.rittmann.common.utils.DateUtil
import com.rittmann.common.utils.EditDecimalFormatController.Companion.DEFAULT_SCALE
import com.rittmann.common.utils.pagination.PageItem
import java.io.Serializable
import java.math.BigDecimal
import java.util.*

/**
 * I'm letting this stuff here cause I wanna see how use it from here
 * */
@Entity(tableName = TableTradeMovement.TABLE)
data class TradeMovement(
    @ColumnInfo(name = TableTradeMovement.ID)
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    @ColumnInfo(name = TableTradeMovement.DATE)
    var date: Calendar = Calendar.getInstance(),

    @ColumnInfo(name = TableTradeMovement.NAME)
    var name: String = "",

    @ColumnInfo(name = TableTradeMovement.TYPE)
    var type: CryptoOperationType = CryptoOperationType.BUY,

    @ColumnInfo(name = TableTradeMovement.OPERATED_AMOUNT)
    var operatedAmount: BigDecimal = BigDecimal(0.0).setScale(DEFAULT_SCALE),

    @ColumnInfo(name = TableTradeMovement.CURRENT_VALUE)
    var currentValue: BigDecimal = BigDecimal(0.0).setScale(DEFAULT_SCALE),

    @ColumnInfo(name = TableTradeMovement.CURRENT_VALUE_CURRENCY)
    var currentValueCurrency: CurrencyType = CurrencyType.REAL,

    @ColumnInfo(name = TableTradeMovement.TOTAL_VALUE)
    var totalValue: BigDecimal = BigDecimal(0.0).setScale(DEFAULT_SCALE),

    @ColumnInfo(name = TableTradeMovement.CONCRETE_TOTAL_VALUE)
    var concreteTotalValue: BigDecimal = BigDecimal(0.0).setScale(DEFAULT_SCALE),

    @ColumnInfo(name = TableTradeMovement.TOTAL_VALUE_CURRENCY)
    var totalValueCurrency: CurrencyType = CurrencyType.REAL,

    @ColumnInfo(name = TableTradeMovement.TAX)
    var tax: BigDecimal = BigDecimal(0.0).setScale(DEFAULT_SCALE),

    @ColumnInfo(name = TableTradeMovement.TAX_CURRENCY)
    var taxCurrency: CurrencyType = CurrencyType.CRYPTO,
) : Serializable, Ponjo(), PageItem {
    override fun getIdentification(): String = id.toString()

    @Transient
    var useThisDate: Boolean = false

    @ColumnInfo(name = TableTradeMovement.CRYPTO_COIN)
    var cryptoCoin: String = ""

    override fun isInserting(): Boolean = id == 0L

    fun calculateTotalValue(concreteValue: Boolean = false): Double =
        if (totalValueCurrency == CurrencyType.REAL)
            if (concreteValue)
                concreteTotalValue.toDouble()
            else
                totalValue.toDouble()
        else {
            operatedAmount.toDouble() * currentValue.toDouble()
        }

    fun calculateTaxValue(): Double =
        if (taxCurrency == CurrencyType.REAL) {
            tax.toDouble()
        } else
            tax.toDouble() * currentValue.toDouble()

    fun calculateTotalsAndTax(
        concreteValue: Boolean = false,
        callback: (total: Double, taxAsCurrency: Double, taxAsCoins: Double) -> Unit
    ) {
        callback(
            calculateTotalValue(concreteValue),
            calculateTaxValue(),
            if (taxCurrency == CurrencyType.CRYPTO) tax.toDouble() else 0.0
        )
    }

    fun calculateTotalValueToDeposit() {
        currentValue = totalValue
        concreteTotalValue = totalValue
    }

    fun updateCryptoCoinWithName() {
        cryptoCoin = name
    }

    companion object {
        fun buy(
            name: String,
            date: String,
            operatedAmount: Double,
            currentValue: Double,
            totalValue: Double,
            concreteTotalValue: Double = -1.0,
            tax: Double
        ) = TradeMovement(
            name = name,
            date = DateUtil.parseDate(date),
            type = CryptoOperationType.BUY,
            operatedAmount = operatedAmount.toBigDecimal(),
            currentValue = currentValue.toBigDecimal(),
            currentValueCurrency = CurrencyType.REAL,
            totalValue = totalValue.toBigDecimal(),

            concreteTotalValue =
            if (concreteTotalValue == -1.0) totalValue.toBigDecimal()
            else concreteTotalValue.toBigDecimal(),

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
        ) = TradeMovement(
            name = name,
            date = DateUtil.parseDate(date),
            type = CryptoOperationType.SELL,
            operatedAmount = operatedAmount.toBigDecimal(),
            currentValue = currentValue.toBigDecimal(),
            currentValueCurrency = CurrencyType.REAL,
            totalValue = totalValue.toBigDecimal(),
            concreteTotalValue = totalValue.toBigDecimal(),
            totalValueCurrency = CurrencyType.REAL,
            tax = tax.toBigDecimal(),
            taxCurrency = CurrencyType.REAL
        )

        fun deposit(
            date: String,
            totalValue: Double = 0.0
        ) = TradeMovement(
            name = TradeMovementOperationTypeName.DEPOSIT.value,
            date = DateUtil.parseDate(date),
            type = CryptoOperationType.DEPOSIT,
            operatedAmount = 1.toBigDecimal(),
            currentValue = totalValue.toBigDecimal(),
            currentValueCurrency = CurrencyType.REAL,
            totalValue = totalValue.toBigDecimal(),
            concreteTotalValue = totalValue.toBigDecimal(),
            totalValueCurrency = CurrencyType.REAL,
            taxCurrency = CurrencyType.REAL
        )

        fun deposit() = TradeMovement(
            name = TradeMovementOperationTypeName.DEPOSIT.value,
            type = CryptoOperationType.DEPOSIT,
            operatedAmount = 1.toBigDecimal(),
            currentValueCurrency = CurrencyType.REAL,
            totalValueCurrency = CurrencyType.REAL,
            taxCurrency = CurrencyType.REAL
        )

        fun withdraw(
            date: String,
            totalValue: Double = 0.0
        ) = TradeMovement(
            date = DateUtil.parseDate(date),
            name = TradeMovementOperationTypeName.WITHDRAW.value,
            type = CryptoOperationType.WITHDRAW,
            operatedAmount = 1.toBigDecimal(),
            currentValueCurrency = CurrencyType.REAL,
            totalValue = totalValue.toBigDecimal(),
            concreteTotalValue = totalValue.toBigDecimal(),
            totalValueCurrency = CurrencyType.REAL,
            taxCurrency = CurrencyType.REAL
        )
    }
}

enum class CryptoOperationType(val value: String) : Serializable {
    BUY("B"), SELL("S"), DEPOSIT("D"), WITHDRAW("W");

    companion object {
        fun convert(valueToConvert: String): CryptoOperationType {
            return when (valueToConvert) {
                BUY.value -> BUY
                SELL.value -> SELL
                WITHDRAW.value -> WITHDRAW
                else -> DEPOSIT
            }
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

enum class TradeMovementOperationTypeName(val value: String) {
    CRYPTO("CRYPTO"),
    DEPOSIT("DEPOSIT"),
    WITHDRAW("WITHDRAW"),
}