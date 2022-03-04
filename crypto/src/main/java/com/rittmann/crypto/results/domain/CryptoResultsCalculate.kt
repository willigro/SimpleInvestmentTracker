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

        var totalDeposited = 0.0
        var totalDepositedWithFees = 0.0
        var totalDepositedFeesAsCurrency = 0.0
        var totalDepositedFeesAsCoins = 0.0

        var totalWithdraw = 0.0
        var totalWithdrawWithFees = 0.0
        var totalWithdrawFeesAsCurrency = 0.0
        var totalWithdrawFeesAsCoins = 0.0

        var totalEarned = 0.0
        var realTotalEarned = 0.0
        var feesAsCurrency = 0.0
        var feesAsCoins = 0.0
        var soldAmount = 0.0

        var concreteTotalInvested = 0.0
        var totalInvested = 0.0
        var totalBoughtAmount = 0.0
        var totalBoughtAmountWithFees = 0.0
        var boughtFeesAsCurrency = 0.0
        var boughtFeesAsCoins = 0.0

        var feesPaidAsCurrency = 0.0
        var feesPaidAsCoins = 0.0

        var isDeposit = true
        var isWithDraw = true

        for (trade in data) {
            when (trade.type) {
                CryptoOperationType.DEPOSIT -> {
                    trade.calculateTotalsAndTax { total, taxAsCurrency, taxAsCoins ->
                        totalDeposited += total
                        totalDepositedWithFees += total + taxAsCurrency
                        totalDepositedFeesAsCurrency += taxAsCurrency
                        totalDepositedFeesAsCoins += taxAsCoins

                        feesPaidAsCurrency += taxAsCurrency
                        feesPaidAsCoins += taxAsCoins
                    }
                }
                CryptoOperationType.WITHDRAW -> {
                    trade.calculateTotalsAndTax { total, taxAsCurrency, taxAsCoins ->
                        totalWithdraw += total
                        totalWithdrawWithFees += total + taxAsCurrency
                        totalWithdrawFeesAsCurrency += taxAsCurrency
                        totalWithdrawFeesAsCoins += taxAsCoins

                        feesPaidAsCurrency += taxAsCurrency
                        feesPaidAsCoins += taxAsCoins
                    }
                }
                CryptoOperationType.BUY -> {
                    trade.calculateTotalsAndTax(true) { total, taxAsCurrency, taxAsCoins ->
                        concreteTotalInvested += total
                        boughtFeesAsCurrency += taxAsCurrency
                        boughtFeesAsCoins += taxAsCoins
                        totalBoughtAmountWithFees += trade.operatedAmount.toDouble() - taxAsCurrency

                        feesPaidAsCurrency += taxAsCurrency
                        feesPaidAsCoins += taxAsCoins
                    }
                    totalInvested += trade.calculateTotalValue(false)
                    totalBoughtAmount += trade.operatedAmount.toDouble()
                    isDeposit = false
                    isWithDraw = false
                }
                CryptoOperationType.SELL -> {
                    trade.calculateTotalsAndTax { total, taxAsCurrency, taxAsCoins ->
                        totalEarned += total
                        realTotalEarned += total - taxAsCurrency
                        feesAsCurrency += taxAsCurrency
                        feesAsCoins += taxAsCoins

                        feesPaidAsCurrency += taxAsCurrency
                        feesPaidAsCoins += taxAsCoins
                    }
                    soldAmount += trade.operatedAmount.toDouble()
                    isDeposit = false
                    isWithDraw = false
                }
            }
        }

        val onHandTotalConcrete: Double = realTotalEarned - concreteTotalInvested

        val onHandAmount: Double = soldAmount - totalBoughtAmount

        return cryptoResultViewBinding.apply {
            _totalDeposited.value = Pair(totalDeposited, CurrencyType.REAL)
            _totalDepositedLeesFees.value = Pair(totalDepositedWithFees, CurrencyType.REAL)
            _totalDepositedFeesAsCurrency.value = Pair(totalDepositedFeesAsCurrency, CurrencyType.REAL)
            _totalDepositedFeesAsCoins.value = Pair(totalDepositedFeesAsCoins, CurrencyType.DECIMAL)

            _totalWithdraw.value = Pair(totalWithdraw, CurrencyType.REAL)
            _totalWithdrawLeesFees.value = Pair(totalWithdrawWithFees, CurrencyType.REAL)
            _totalWithdrawFeesAsCurrency.value = Pair(totalWithdrawFeesAsCurrency, CurrencyType.REAL)
            _totalWithdrawFeesAsCoins.value = Pair(totalWithdrawFeesAsCoins, CurrencyType.DECIMAL)

            _totalEarned.value = Pair(totalEarned, CurrencyType.REAL)
            _totalRealEarned.value = Pair(realTotalEarned, CurrencyType.REAL)
            _totalFeesAsCurrencyFromEarned.value = Pair(feesAsCurrency, CurrencyType.REAL)
            _totalFeesAsCoinsFromEarned.value = Pair(feesAsCoins, CurrencyType.DECIMAL)
            _souldAmount.value = Pair(soldAmount, CurrencyType.DECIMAL)

            _totalConcreteInvested.value = Pair(concreteTotalInvested, CurrencyType.REAL)
            _totalInvested.value = Pair(totalInvested, CurrencyType.REAL)
            _totalBoughtFeesAsCurrency.value = Pair(boughtFeesAsCurrency, CurrencyType.REAL)
            _totalBoughtFeesAsCoins.value = Pair(boughtFeesAsCoins, CurrencyType.DECIMAL)
            _totalBoughtAmount.value = Pair(totalBoughtAmount, CurrencyType.DECIMAL)
            _totalBoughtAmountWithFees.value = Pair(totalBoughtAmountWithFees, CurrencyType.DECIMAL)

            _totalOnHandConcrete.value = Pair(onHandTotalConcrete, CurrencyType.REAL)
            _totalOnHandAmount.value = Pair(onHandAmount, CurrencyType.DECIMAL)
            _totalFeesPaidAsCurrency.value = Pair(feesPaidAsCurrency, CurrencyType.REAL)
            _totalFeesPaidAsCoins.value = Pair(feesPaidAsCoins, CurrencyType.DECIMAL)

            _isDepositOrWithdraw.value = isDeposit || isWithDraw
        }
    }

    fun calculateTotalOnHand(deposit: Double, earned: Double, invested: Double, withdraw: Double) =
        earned - invested
//        (earned + deposit) - invested
//        earned + (deposit - invested)
}