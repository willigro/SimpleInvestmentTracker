package com.rittmann.common_test.mock

import com.rittmann.datasource.basic.CryptoMovement
import com.rittmann.datasource.basic.CryptoOperationType
import com.rittmann.datasource.basic.CurrencyType
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
    newCryptoMovementMock.copy(id = 2L)
)

val exceptionMock = Exception()