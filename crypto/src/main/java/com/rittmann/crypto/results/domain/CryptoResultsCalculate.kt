package com.rittmann.crypto.results.domain

import com.rittmann.common.datasource.basic.TradeMovement
import com.rittmann.common.datasource.basic.CryptoOperationType
import com.rittmann.common.datasource.basic.CurrencyType
import com.rittmann.crypto.results.ui.CryptoResultViewBinding

object CryptoResultsCalculate {

    fun calculateResults(
        cryptoResultViewBinding: CryptoResultViewBinding,
        data: List<TradeMovement>
    ): CryptoResultViewBinding {

        var earned = 0.0
        var invested = 0.0
        var boughtAmount = 0.0
        var soldAmount = 0.0
        var taxPaid = 0.0
        var taxAmount = 0.0

        data.forEach {
            if (it.type == CryptoOperationType.BUY) {
                invested += it.calculateTotalValue()
                taxPaid += it.calculateTaxValue()
                boughtAmount += it.operatedAmount.toDouble()
            } else {
                earned += it.calculateTotalValue()
                taxPaid += it.calculateTaxValue()
                soldAmount += it.operatedAmount.toDouble()
            }

            if (it.taxCurrency == CurrencyType.CRYPTO)
                taxAmount += it.tax.toDouble()
        }

        val onHand: Double = earned - invested
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
        }
    }
}