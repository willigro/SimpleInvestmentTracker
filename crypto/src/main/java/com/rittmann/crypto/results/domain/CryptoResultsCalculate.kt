package com.rittmann.crypto.results.domain

import com.rittmann.common.datasource.basic.TradeMovement
import com.rittmann.common.datasource.basic.CryptoOperationType
import com.rittmann.common.datasource.basic.CurrencyType
import com.rittmann.crypto.results.ui.CryptoResultViewBinding

object CryptoResultsCalculate {

    fun calculateResults(
        cryptoResultViewBinding: CryptoResultViewBinding,
        data: List<TradeMovement>,

        ): CryptoResultViewBinding {

        var earned = 0.0
        var invested = 0.0
        var boughtAmount = 0.0
        var soldAmount = 0.0
        var taxPaid = 0.0
        var taxAmount = 0.0
        var totalDeposited = 0.0
        var totalWithdraw = 0.0

        var isDeposit = true
        var isWithDraw = true

        for (trade in data) {
            when (trade.type) {
                CryptoOperationType.DEPOSIT -> {
                    totalDeposited += trade.calculateTotalValue()
                    taxPaid += trade.calculateTaxValue()
                }
                CryptoOperationType.WITHDRAW -> {
                    totalWithdraw += trade.calculateTotalValue()
                    taxPaid += trade.calculateTaxValue()
                }
                CryptoOperationType.BUY -> {
                    invested += trade.calculateTotalValue()
                    taxPaid += trade.calculateTaxValue()
                    boughtAmount += trade.operatedAmount.toDouble()
                    isDeposit = false
                    isWithDraw = false
                }
                CryptoOperationType.SELL -> {
                    earned += trade.calculateTotalValue()
                    taxPaid += trade.calculateTaxValue()
                    soldAmount += trade.operatedAmount.toDouble()
                    isDeposit = false
                    isWithDraw = false
                }
            }

            if (trade.taxCurrency == CurrencyType.CRYPTO)
                taxAmount += trade.tax.toDouble()
        }

        val onHand: Double = calculateTotalOnHand(totalDeposited, earned, invested, totalWithdraw)
        val onHandWithoutTax: Double = onHand - taxPaid
        val onHandAmount: Double = boughtAmount - soldAmount
        val amountOnHandWithoutTax: Double = onHandAmount - taxAmount

        return cryptoResultViewBinding.apply {
            _totalOnHand.value = Pair(onHand, CurrencyType.REAL)
            _totalOnHandWithoutTax.value = Pair(onHandWithoutTax, CurrencyType.REAL)
            _totalEarned.value = Pair(earned, CurrencyType.REAL)
            _totalInvested.value = Pair(invested, CurrencyType.REAL)
            _totalBoughtAmount.value = Pair(boughtAmount, CurrencyType.DECIMAL)
            _totalSoldAmount.value = Pair(soldAmount, CurrencyType.DECIMAL)
            _totalOnHandAmount.value = Pair(onHandAmount, CurrencyType.DECIMAL)
            _totalTaxPaid.value = Pair(taxPaid, CurrencyType.REAL)
            _totalTaxAmount.value = Pair(taxAmount, CurrencyType.DECIMAL)
            _totalAmountOnHandWithoutTax.value = Pair(amountOnHandWithoutTax, CurrencyType.DECIMAL)
            _totalDeposited.value = Pair(totalDeposited, CurrencyType.REAL)
            _totalWithdraw.value = Pair(totalWithdraw, CurrencyType.REAL)
            _isDepositOrWithdraw.value = isDeposit || isWithDraw
        }
    }

    fun calculateTotalOnHand(deposit: Double, earned: Double, invested: Double, withdraw: Double) =
        earned + (deposit - invested) - withdraw
}