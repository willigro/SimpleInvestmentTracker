package com.rittmann.crypto.results.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rittmann.common.datasource.basic.CurrencyType
import com.rittmann.common.extensions.orZero
import com.rittmann.common.utils.EditDecimalFormatController.Companion.DEFAULT_SCALE
import com.rittmann.common.utils.FormatDecimalController
import com.rittmann.common.utils.transformerIt

class CryptoResultViewBinding {
    val _totalOnHand: MutableLiveData<Pair<Double, CurrencyType>> = MutableLiveData()
    val totalOnHand: LiveData<String>
        get() = transformerIt<Pair<Double, CurrencyType>, String>(_totalOnHand) {
            FormatDecimalController.format(it?.first?.orZero()!!, CurrencyType.REAL, DEFAULT_SCALE)
        }

    val _totalOnHandWithoutTax: MutableLiveData<Pair<Double, CurrencyType>> = MutableLiveData()
    val totalOnHandWithoutTax: LiveData<String>
        get() = transformerIt<Pair<Double, CurrencyType>, String>(_totalOnHandWithoutTax) {
            FormatDecimalController.format(it?.first?.orZero()!!, CurrencyType.REAL, DEFAULT_SCALE)
        }

    val _totalEarned: MutableLiveData<Pair<Double, CurrencyType>> = MutableLiveData()
    val totalEarned: LiveData<String>
        get() = transformerIt<Pair<Double, CurrencyType>, String>(_totalEarned) {
            FormatDecimalController.format(it?.first?.orZero()!!, CurrencyType.REAL, DEFAULT_SCALE)
        }

    val _totalInvested: MutableLiveData<Pair<Double, CurrencyType>> = MutableLiveData()
    val totalInvested: LiveData<String>
        get() = transformerIt<Pair<Double, CurrencyType>, String>(_totalInvested) {
            FormatDecimalController.format(it?.first?.orZero()!!, CurrencyType.REAL, DEFAULT_SCALE)
        }

    val _totalBoughtAmount: MutableLiveData<Pair<Double, CurrencyType>> = MutableLiveData()
    val totalBoughtAmount: LiveData<String>
        get() = transformerIt<Pair<Double, CurrencyType>, String>(_totalBoughtAmount) {
            FormatDecimalController.format(it?.first?.orZero()!!, CurrencyType.REAL, DEFAULT_SCALE)
        }

    val _totalSoldAmount: MutableLiveData<Pair<Double, CurrencyType>> = MutableLiveData()
    val totalSoldAmount: LiveData<String>
        get() = transformerIt<Pair<Double, CurrencyType>, String>(_totalSoldAmount) {
            FormatDecimalController.format(it?.first?.orZero()!!, CurrencyType.REAL, DEFAULT_SCALE)
        }

    val _totalOnHandAmount: MutableLiveData<Pair<Double, CurrencyType>> = MutableLiveData()
    val totalOnHandAmount: LiveData<String>
        get() = transformerIt<Pair<Double, CurrencyType>, String>(_totalOnHandAmount) {
            FormatDecimalController.format(it?.first?.orZero()!!, CurrencyType.REAL, DEFAULT_SCALE)
        }

    val _totalTaxPaid: MutableLiveData<Pair<Double, CurrencyType>> = MutableLiveData()
    val totalTaxPaid: LiveData<String>
        get() = transformerIt<Pair<Double, CurrencyType>, String>(_totalTaxPaid) {
            FormatDecimalController.format(it?.first?.orZero()!!, CurrencyType.REAL, DEFAULT_SCALE)
        }
}