package com.rittmann.crypto.results.domain

import com.rittmann.common.datasource.basic.CryptoMovement
import com.rittmann.common.datasource.basic.CryptoOperationType
import com.rittmann.common.datasource.basic.CurrencyType
import com.rittmann.crypto.results.ui.CryptoResultViewBinding

object CryptoResultsCalculate {

    fun calculateResults(
        cryptoResultViewBinding: CryptoResultViewBinding,
        data: List<CryptoMovement>
    ): CryptoResultViewBinding {

        var earned = 0.0
        var invested = 0.0
        var boughtAmount = 0.0
        var soldAmount = 0.0
        var taxPaid = 0.0

        data.forEach {
            if (it.type == CryptoOperationType.BUY) {
                invested += if (it.totalValueCurrency == CurrencyType.REAL)
                    it.totalValue
                else {
                    it.calculateTotalValue()
                }

                taxPaid += if (it.taxCurrency == CurrencyType.REAL) {
                    it.tax
                } else
                    it.calculateTaxValue()

                boughtAmount += it.operatedAmount
            } else {
                earned += if (it.totalValueCurrency == CurrencyType.REAL)
                    it.totalValue
                else {
                    it.calculateTotalValue()
                }

                taxPaid += if (it.taxCurrency == CurrencyType.REAL) {
                    it.tax
                } else
                    it.calculateTaxValue()

                soldAmount += it.operatedAmount
            }
        }

        val onHand: Double = earned - invested
        val onHandWithoutTax: Double = onHand - taxPaid
        val onHandAmount: Double = boughtAmount - soldAmount

        return cryptoResultViewBinding.apply {
            _totalOnHand.value = Pair(onHand, CurrencyType.REAL)
            _totalOnHandWithoutTax.value = Pair(onHandWithoutTax, CurrencyType.REAL)
            _totalEarned.value = Pair(earned, CurrencyType.REAL)
            _totalInvested.value = Pair(invested, CurrencyType.REAL)
            _totalBoughtAmount.value = Pair(boughtAmount, CurrencyType.REAL)
            _totalSoldAmount.value = Pair(soldAmount, CurrencyType.REAL)
            _totalOnHandAmount.value = Pair(onHandAmount, CurrencyType.REAL)
            _totalTaxPaid.value = Pair(taxPaid, CurrencyType.REAL)
        }
    }
}