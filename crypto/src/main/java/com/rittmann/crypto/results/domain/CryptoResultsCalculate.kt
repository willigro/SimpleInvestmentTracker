package com.rittmann.crypto.results.domain

import com.rittmann.common.datasource.basic.CryptoMovement
import com.rittmann.common.datasource.basic.CryptoOperationType
import com.rittmann.crypto.results.ui.CryptoResultViewBinding

object CryptoResultsCalculate {

     fun calculateResults(cryptoResultViewBinding: CryptoResultViewBinding, data: List<CryptoMovement>) : CryptoResultViewBinding {

        var earned = 0.0
        var invested = 0.0
        var boughtAmount = 0.0
        var soldAmount = 0.0
        var taxPaid = 0.0

        data.forEach {
            if (it.type == CryptoOperationType.BUY) {
                invested += it.totalValue
                boughtAmount += it.operatedAmount
                taxPaid += it.tax
            } else {
                earned += it.totalValue
                soldAmount += it.operatedAmount
                taxPaid += it.tax
            }
        }

        val onHand: Double = earned - invested
        val onHandWithoutTax: Double = onHand - taxPaid
        val onHandAmount: Double = boughtAmount - soldAmount

        return cryptoResultViewBinding.apply {
            totalOnHand.value = onHand.toString()
            totalOnHandWithoutTax.value = onHandWithoutTax.toString()
            totalEarned.value = earned.toString()
            totalInvested.value = invested.toString()
            totalBoughtAmount.value = boughtAmount.toString()
            totalSoldAmount.value = soldAmount.toString()
            totalOnHandAmount.value = onHandAmount.toString()
            totalTaxPaid.value = taxPaid.toString()
        }
    }
}