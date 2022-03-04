package com.rittmann.crypto.results.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rittmann.common.datasource.basic.CurrencyType
import com.rittmann.common.extensions.orZero
import com.rittmann.common.utils.EditDecimalFormatController.Companion.SCALE_LIMIT
import com.rittmann.common.utils.FormatDecimalController
import com.rittmann.common.utils.transformerIt

class CryptoResultViewBinding {

    val _isDepositOrWithdraw: MutableLiveData<Boolean> = MutableLiveData()
    val isDeposit: LiveData<Boolean>
        get() = _isDepositOrWithdraw

    // ON HAND

    val _totalOnHandConcrete: MutableLiveData<Pair<Double, CurrencyType?>> = MutableLiveData()
    val totalOnHandConcrete: LiveData<String>
        get() = transformerIt<Pair<Double, CurrencyType?>, String>(_totalOnHandConcrete) {
            format(it)
        }

    val _totalOnHandAmount: MutableLiveData<Pair<Double, CurrencyType?>> = MutableLiveData()
    val totalOnHandAmount: LiveData<String>
        get() = transformerIt<Pair<Double, CurrencyType?>, String>(_totalOnHandAmount) {
            format(it)
        }

    // FEES

    val _totalFeesPaidAsCurrency: MutableLiveData<Pair<Double, CurrencyType?>> = MutableLiveData()
    val totalFeesPaidAsCurrency: LiveData<String>
        get() = transformerIt<Pair<Double, CurrencyType?>, String>(_totalFeesPaidAsCurrency) {
            format(it)
        }

    val _totalFeesPaidAsCoins: MutableLiveData<Pair<Double, CurrencyType?>> = MutableLiveData()
    val totalFeesPaidAsCoins: LiveData<String>
        get() = transformerIt<Pair<Double, CurrencyType?>, String>(_totalFeesPaidAsCoins) {
            format(it)
        }

    // EARNED

    val _totalEarned: MutableLiveData<Pair<Double, CurrencyType?>> = MutableLiveData()
    val totalEarned: LiveData<String>
        get() = transformerIt<Pair<Double, CurrencyType?>, String>(_totalEarned) {
            format(it)
        }

    val _totalRealEarned: MutableLiveData<Pair<Double, CurrencyType?>> = MutableLiveData()
    val totalRealEarned: LiveData<String>
        get() = transformerIt<Pair<Double, CurrencyType?>, String>(_totalRealEarned) {
            format(it)
        }

    val _totalFeesAsCurrencyFromEarned: MutableLiveData<Pair<Double, CurrencyType?>> = MutableLiveData()
    val totalFeesAsCurrencyFromEarned: LiveData<String>
        get() = transformerIt<Pair<Double, CurrencyType?>, String>(_totalFeesAsCurrencyFromEarned) {
            format(it)
        }

    val _totalFeesAsCoinsFromEarned: MutableLiveData<Pair<Double, CurrencyType?>> = MutableLiveData()
    val totalFeesAsCoinsFromEarned: LiveData<String>
        get() = transformerIt<Pair<Double, CurrencyType?>, String>(_totalFeesAsCoinsFromEarned) {
            format(it)
        }

    val _souldAmount: MutableLiveData<Pair<Double, CurrencyType?>> = MutableLiveData()
    val souldAmount: LiveData<String>
        get() = transformerIt<Pair<Double, CurrencyType?>, String>(_souldAmount) {
            format(it)
        }

    // INVESTED

    val _totalInvested: MutableLiveData<Pair<Double, CurrencyType?>> = MutableLiveData()
    val totalInvested: LiveData<String>
        get() = transformerIt<Pair<Double, CurrencyType?>, String>(_totalInvested) {
            format(it)
        }

    val _totalConcreteInvested: MutableLiveData<Pair<Double, CurrencyType?>> = MutableLiveData()
    val totalConcreteInvested: LiveData<String>
        get() = transformerIt<Pair<Double, CurrencyType?>, String>(_totalConcreteInvested) {
            format(it)
        }

    val _totalBoughtAmount: MutableLiveData<Pair<Double, CurrencyType?>> = MutableLiveData()
    val totalBoughtAmount: LiveData<String>
        get() = transformerIt<Pair<Double, CurrencyType?>, String>(_totalBoughtAmount) {
            format(it)
        }

    val _totalBoughtAmountWithFees: MutableLiveData<Pair<Double, CurrencyType?>> = MutableLiveData()
    val totalBoughtAmountWithFees: LiveData<String>
        get() = transformerIt<Pair<Double, CurrencyType?>, String>(_totalBoughtAmountWithFees) {
            format(it)
        }

    val _totalBoughtFeesAsCurrency: MutableLiveData<Pair<Double, CurrencyType?>> = MutableLiveData()
    val totalBoughtFeesAsCurrency: LiveData<String>
        get() = transformerIt<Pair<Double, CurrencyType?>, String>(_totalBoughtFeesAsCurrency) {
            format(it)
        }

    val _totalBoughtFeesAsCoins: MutableLiveData<Pair<Double, CurrencyType?>> = MutableLiveData()
    val totalBoughtFeesAsCoins: LiveData<String>
        get() = transformerIt<Pair<Double, CurrencyType?>, String>(_totalBoughtFeesAsCoins) {
            format(it)
        }

    // DEPOSIT

    val _totalDeposited: MutableLiveData<Pair<Double, CurrencyType?>> = MutableLiveData()
    val totalDeposited: LiveData<String>
        get() = transformerIt<Pair<Double, CurrencyType?>, String>(_totalDeposited) {
            format(it)
        }

    val _totalDepositedLeesFees: MutableLiveData<Pair<Double, CurrencyType?>> = MutableLiveData()
    val totalDepositedLeesFees: LiveData<String>
        get() = transformerIt<Pair<Double, CurrencyType?>, String>(_totalDepositedLeesFees) {
            format(it)
        }

    val _totalDepositedFeesAsCurrency: MutableLiveData<Pair<Double, CurrencyType?>> = MutableLiveData()
    val totalDepositedFeesAsCurrency: LiveData<String>
        get() = transformerIt<Pair<Double, CurrencyType?>, String>(_totalDepositedFeesAsCurrency) {
            format(it)
        }

    val _totalDepositedFeesAsCoins: MutableLiveData<Pair<Double, CurrencyType?>> = MutableLiveData()
    val totalDepositedFeesAsCoins: LiveData<String>
        get() = transformerIt<Pair<Double, CurrencyType?>, String>(_totalDepositedFeesAsCoins) {
            format(it)
        }

    // WITHDRAW

    val _totalWithdraw: MutableLiveData<Pair<Double, CurrencyType?>> = MutableLiveData()
    val totalWithdraw: LiveData<String>
        get() = transformerIt<Pair<Double, CurrencyType?>, String>(_totalWithdraw) {
            format(it)
        }

    val _totalWithdrawLeesFees: MutableLiveData<Pair<Double, CurrencyType?>> = MutableLiveData()
    val totalWithdrawLeesFees: LiveData<String>
        get() = transformerIt<Pair<Double, CurrencyType?>, String>(_totalWithdrawLeesFees) {
            format(it)
        }

    val _totalWithdrawFeesAsCurrency: MutableLiveData<Pair<Double, CurrencyType?>> = MutableLiveData()
    val totalWithdrawFeesAsCurrency: LiveData<String>
        get() = transformerIt<Pair<Double, CurrencyType?>, String>(_totalWithdrawFeesAsCurrency) {
            format(it)
        }

    val _totalWithdrawFeesAsCoins: MutableLiveData<Pair<Double, CurrencyType?>> = MutableLiveData()
    val totalWithdrawFeesAsCoins: LiveData<String>
        get() = transformerIt<Pair<Double, CurrencyType?>, String>(_totalWithdrawFeesAsCoins) {
            format(it)
        }

    private fun format(pair: Pair<Double, CurrencyType?>?) = FormatDecimalController.format(
        pair?.first.orZero(),
        pair?.second ?: CurrencyType.REAL,
        SCALE_LIMIT
    )
}