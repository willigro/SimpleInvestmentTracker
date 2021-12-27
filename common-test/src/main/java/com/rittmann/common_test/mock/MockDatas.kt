package com.rittmann.common_test.mock

import com.rittmann.common.datasource.basic.CryptoMovement
import com.rittmann.common.datasource.basic.CryptoOperationType
import com.rittmann.common.datasource.basic.CurrencyType
import java.lang.Exception

val newCryptoMovementMock =
    CryptoMovement(
        id = 0L,
        date = "00/00/0000",
        name = "BTC",
        type = CryptoOperationType.BUY,
        boughtAmount = 2.0,
        currentValue = 10.0,
        currentValueCurrency = CurrencyType.REAL,
        totalValue = 30.0,
        totalValueCurrency = CurrencyType.REAL,
        tax = 1.0,
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
            currentTotalInvested += it.totalValue
        else
            currentTotalEarned += it.totalValue
    }
}

var currentTotalInvested: Double = 0.0
var currentTotalEarned: Double = 0.0

val exceptionMock = Exception()