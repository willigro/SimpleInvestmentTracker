package com.rittmann.common.datasource.dao.mock

import com.rittmann.common.datasource.basic.CryptoMovement
import com.rittmann.common.datasource.basic.CryptoOperationType
import com.rittmann.common.datasource.basic.CurrencyType

val newCrypto = CryptoMovement(
    0L,
    "00/00/0000",
    "BTC",
    CryptoOperationType.BUY,
    2.0,
    10.0,
    CurrencyType.REAL,
    30.0,
    CurrencyType.REAL,
    1.0,
    CurrencyType.REAL
)