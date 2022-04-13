package com.rittmann.common_test.mock

import com.rittmann.common.datasource.basic.TradeMovement
import com.rittmann.common.datasource.basic.CryptoOperationType
import com.rittmann.common.datasource.basic.CurrencyType
import com.rittmann.common.utils.DateUtil
import java.lang.Exception
import java.math.BigDecimal

val newCryptoMovementMock =
    TradeMovement(
        id = 0L,
        date = DateUtil.parseDate("20/12/2020"),
        name = "BTC",
        type = CryptoOperationType.BUY,
        operatedAmount = BigDecimal(2.0),
        currentValue = BigDecimal(10.0),
        currentValueCurrency = CurrencyType.REAL,
        totalValue = BigDecimal(30.0),
        totalValueCurrency = CurrencyType.REAL,
        tax = BigDecimal(1.0),
        taxCurrency = CurrencyType.REAL
    )

val registeredCryptoMovementMock = newCryptoMovementMock.copy(1L)

val listCryptoMovementMock = arrayListOf(
    newCryptoMovementMock,
    newCryptoMovementMock.copy(id = 1L),
    newCryptoMovementMock.copy(id = 2L),
    newCryptoMovementMock.copy(id = 3L, type = CryptoOperationType.SELL),
    newCryptoMovementMock.copy(id = 4L, type = CryptoOperationType.SELL)
).apply {
    forEach {
        if (it.type == CryptoOperationType.BUY)
            currentTotalInvested += it.totalValue.toDouble()
        else
            currentTotalEarned += it.totalValue.toDouble()
    }
}

var currentTotalInvested: Double = 0.0
var currentTotalEarned: Double = 0.0

val exceptionMock = Exception()